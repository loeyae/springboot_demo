export LANG="en_US.UTF-8"
node {
    properties([
            parameters([
                    string(defaultValue: '60', description: '单元测试覆盖率及格线', name: 'deltaInstructionCoverage', trim: false),
                    string(defaultValue: '90', description: '单元测试覆盖率stable line', name: 'maximumInstructionCoverage', trim: false),
                    string(defaultValue: '30', description: '单元测试覆盖率底线', name: 'minimumInstructionCoverage', trim: false),
                    string(defaultValue: '1.0.1', description: 'release版本号', name: 'RELEASE_TAG', trim: false),
                    booleanParam(defaultValue: true, description: '是否必须stable才打包', name: 'PACKAGE_BY_STABLE')
            ])
    ])
    def PACKAGE_BY_STABLE = params.PACKAGE_BY_STABLE ? params.PACKAGE_BY_STABLE : false
    def deltaInstructionCoverage = params.deltaInstructionCoverage ? params.deltaInstructionCoverage : '60'
    def maximumInstructionCoverage = params.maximumInstructionCoverage ? params.maximumInstructionCoverage : '90'
    def minimumInstructionCoverage = params.minimumInstructionCoverage ? params.minimumInstructionCoverage : '30'
    stage("Checkout") {
        checkout(
                [
                        $class                           : 'GitSCM',
                        branches                         : [[name: '*/master']],
                        browser                          : [$class: 'GithubWeb', repoUrl: 'https://github.com/loeyae/springboot_demo'],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [],
                        submoduleCfg                     : [],
                        userRemoteConfigs                : [[credentialsId: 'github', url: 'git@github.com:loeyae/springboot_demo.git']]
                ]
        )
    }
//    stage("code analysis") {
//        withSonarQubeEnv('Sonarqube') {
//            //固定使用项目根目录${basedir}下的pom.xml进行代码检查
//            sh "mvn -f pom.xml clean compile sonar:sonar "
//        }
//    }
    stage("Analysis & Unit test") {
        withSonarQubeEnv("Sonarqube") {
//            sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent -f pom.xml clean test -Dautoconfig" +
//                    ".skip=true -Dmaven.test.skip=false -Dmaven.test.failure.ignore=true sonar:sonar"
            //配置jacoco到pom.xml
            sh "mvn -f pom.xml clean test -Dautoconfig.skip=true -Dmaven.test.failure.ignore=true sonar:sonar"
            junit healthScaleFactor: 10.0, testResults: '**/target/surefire-reports/*.xml'
            //整合覆盖率到jenkins
            publishCoverage ([
                    adapters: [jacocoAdapter('**/target/jacoco-ut/*.xml')],
                    sourceFileResolver: sourceFiles('NEVER_STORE')
            ])
            //单元测死覆盖率控制任务晴雨表
            jacoco([
                    buildOverBuild: true,
                    changeBuildStatus: true,
                    deltaInstructionCoverage: deltaInstructionCoverage,   //低于该覆盖率时，任务状态为unstable
                    maximumInstructionCoverage: maximumInstructionCoverage, //高于该覆盖率时，任务状态为stable
                    minimumInstructionCoverage: minimumInstructionCoverage  //低于该覆盖率时，任务状态为failure
                ])
        }
        timeout(1) {
            waitForQualityGate abortPipeline: true
        }
    }
    stage("Tag") {
        if ((PACKAGE_BY_STABLE && currentBuild.resultIsBetterOrEqualTo("SUCCESS")) ||
                (!PACKAGE_BY_STABLE && currentBuild.resultIsBetterOrEqualTo("UNSTABLE"))) {
            try {
                def tag = "release-${params.RELEASE_TAG}.${env.BUILD_NUMBER}"
                sshagent(["github-ssh"]) {
                    sh """
                git config user.email 'loeyae@gmail.com'
                git config user.name 'ZhangYi'
                git tag -a -m 'add release tag' $tag
                git push origin $tag
                """
                }
            }
            catch (exc) {
                println("Tag failure")
                print(exc.getMessage())
                currentBuild.result = 'FAILURE'
            }
        } else {
            echo "Task FAILURE, Skip tag"
        }
    }
    stage("Package") {
        if ((PACKAGE_BY_STABLE && currentBuild.resultIsBetterOrEqualTo("SUCCESS")) ||
                (!PACKAGE_BY_STABLE && currentBuild.resultIsBetterOrEqualTo("UNSTABLE"))) {
            withCredentials([dockerCert(credentialsId: 'docker-local', variable: 'DOCKER_CERT_PATH')]) {
                sh "mvn -f pom.xml clean package -Dautoconfig.skip=true -Dmaven.test.skip=true"
            }

        } else {
            echo "Task FAILURE, Skip package"
        }
    }
    stage("Image push") {
        def imageTag = "loeyae/springboot_demo:${env.BUILD_NUMBER}"
        def latestTag = "loeyae/springboot_demo:latest"
        if ((PACKAGE_BY_STABLE && currentBuild.resultIsBetterOrEqualTo("SUCCESS")) ||
                (!PACKAGE_BY_STABLE && currentBuild.resultIsBetterOrEqualTo("UNSTABLE"))) {
            withCredentials([dockerCert(credentialsId: 'docker-local', variable: 'DOCKER_CERT_PATH')]) {
                try {
                    sh """
                  docker tag springboot_demo:latest $imageTag
                  docker tag springboot_demo:latest $latestTag
                  docker push $imageTag
                  docker push $latestTag
                  """
                }
                catch (exc) {
                    println("Push image failure")
                    print(exc.getMessage())
                    currentBuild.result = 'FAILURE'
                }
                sh """
                docker rmi $imageTag
                docker rmi $latestTag
                """
            }
        } else {
            echo "Task FAILURE, Skip image push"
        }
    }
}
