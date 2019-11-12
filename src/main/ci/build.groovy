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
            sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent -f pom.xml clean test -Dautoconfig" +
                    ".skip=true -Dmaven.test.skip=false -Dmaven.test.failure.ignore=true sonar:sonar"
            junit '**/target/surefire-reports/*.xml'
//            jacoco([
//                    buildOverBuild: true,
//                    changeBuildStatus: true,
//                    deltaInstructionCoverage: '60',
//                    maximumInstructionCoverage: '90',
//                    minimumInstructionCoverage: '70'
//                ])
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
        if (currentBuild.result != 'FAILURE') {
            try {
                def imageTag = env.BUILD_NUMBER
                sh """
                          docker tag springboot_demo:latest loeyae/springboot_demo:$imageTag
                          docker push loeyae/springboot_demo:$imageTag
                          """
            }
            catch (exc) {
                println("Push image failure")
                print(exc.getMessage())
                currentBuild.result = 'FAILURE'
            }
        } else {
            echo "Task FAILURE, Skip image push"
        }
    }
}
