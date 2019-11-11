node {
    stage("checkout") {
        checkout(
                [
                        $class                           : 'GitSCM',
                        branches                         : [[name: '*/master']],
                        browser                          : [$class: 'GithubWeb', repoUrl: 'https://github.com/loeyae/springboot_demo'],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [],
                        submoduleCfg                     : [],
                        userRemoteConfigs                : [[credentialsId: 'github', url: 'https://github.com/loeyae/springboot_demo']]
                ]
        )
    }
//    stage("code analysis") {
//        withSonarQubeEnv('Sonarqube') {
//            //固定使用项目根目录${basedir}下的pom.xml进行代码检查
//            sh "mvn -f pom.xml clean compile sonar:sonar "
//        }
//    }
    stage("analysis & unit test") {
        withSonarQubeEnv("Sonarqube") {
            sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent -f pom.xml clean test -Dautoconfig" +
                    ".skip=true -Dmaven.test.skip=false -Dmaven.test.failure.ignore=true sonar:sonar"
            junit '**/target/surefire-reports/*.xml'
            jacoco buildOverBuild: true, changeBuildStatus: true, deltaInstructionCoverage: '60', maximumInstructionCoverage: '90', minimumInstructionCoverage: '70'
        }
        timeout(1) {
            waitForQualityGate abortPipeline: true
        }
    }
    stage("tag") {
        def tag = "release-${params.RELEASE_TAG}.$BUILD_NUMBER"
        sshagent(["githubssh"]) {
            echo "$tag"
            sh "git config user.email=loeyae@gmail.com"
            sh "git config user.name=Zhang Yi"
            sh "git  -a -m 'add release tag' $tag"
            sh "git push origin $tag"
        }
    }
}
