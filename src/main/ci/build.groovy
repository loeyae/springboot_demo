node {
    stage("Checkout") {
        checkout(
                [
                        $class                           : 'GitSCM',
                        branches                         : [[name: '*/master']],
                        browser                          : [$class: 'GithubWeb', repoUrl: 'https://github.com/loeyae/springboot_demo'],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [],
                        submoduleCfg                     : [],
                        userRemoteConfigs                : [[credentialsId: 'github-ssh', url: 'git@github.com:loeyae/springboot_demo.git']]
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
            sh "mvn -f pom.xml clean test -Dautoconfig.skip=true -Dmaven.test.failure.ignore=true sonar:sonar"
            junit '**/target/surefire-reports/*.xml'
            //整合覆盖率到jenkins
            publishCoverage ([
                    adapters: [jacocoAdapter('**/target/jacoco-ut/*.xml')],
                    sourceFileResolver: sourceFiles('NEVER_STORE')
            ])
            //单元测死覆盖率控制任务晴雨表
            jacoco([
                    buildOverBuild: true,
                    changeBuildStatus: true,
                    deltaInstructionCoverage: '11',   //低于60%覆盖率时，任务状态为unstable
                    maximumInstructionCoverage: '12', //高于90%覆盖率时，任务状态为stable
                    minimumInstructionCoverage: '10'  //低于10%覆盖率时，任务状态为failure
                ])
        }
        timeout(1) {
            waitForQualityGate abortPipeline: true
        }
    }
    stage("Tag") {
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
        catch (exc){
            println("Tag failure")
            print(exc.getMessage())
            currentBuild.result = 'FAILURE'
        }
    }
    stage("Package") {
        println("current build result ${currentBuild.result}")
        if (currentBuild.result != 'FAILURE') {
            sh "mvn -f pom.xml clean package -Dautoconfig.skip=true -Dmaven.test.skip=true"

        } else {
            echo "Task FAILURE, Skip package"
        }
    }
    stage("Image push") {
        def imageTag = "loeyae/springboot_demo:${env.BUILD_NUMBER}"
        def latestTag = "loeyae/springboot_demo:latest"
        if (currentBuild.result != 'FAILURE') {
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
                """
        } else {
            echo "Task FAILURE, Skip image push"
        }
    }
}
