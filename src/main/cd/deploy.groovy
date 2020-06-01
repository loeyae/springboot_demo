node {
    properties([
            parameters([
                    string(defaultValue: 'dev', description: '部署环境标志', name: 'ENV_LABEL', trim:
                            false)
            ])
    ])
    stage("Checkout") {
        checkout(
                [
                        $class                           : 'GitSCM',
                        branches                         : [[name: '*/master']],
                        browser                          : [$class: 'GithubWeb', repoUrl: 'https://github.com/loeyae/springboot_demo'],
                        doGenerateSubmoduleConfigurations: false,
                        extensions                       : [],
                        submoduleCfg                     : [],
                        userRemoteConfigs                : [[credentialsId: 'github', url: 'https://github.com/loeyae/springboot_demo.git']]
                ]
        )
    }
    stage("deploy") {
        def buildId = currentBuild.previousBuiltBuild.id
        println("previousBuiltBuild id: $buildId")
        sh"env TAG=${buildId}"
        sh "kubectl apply -f src/main/cd/deploy-${params.ENV_LABEL}.yml"
    }
}