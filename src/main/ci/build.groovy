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
    stage("unit test") {
        sh "mvn org.jacoco:jacoco-maven-plugin:prepare-agent -f pom.xml clean test -Dautoconfig" +
                ".skip=true -Dmaven.test.skip=false -Dmaven.test.failure.ignore=true"
        junit '**/target/surefire-reports/*.xml'
        jacoco buildOverBuild: true, changeBuildStatus: true, deltaInstructionCoverage: '60', maximumInstructionCoverage: '90', minimumInstructionCoverage: '70'
    }
}
