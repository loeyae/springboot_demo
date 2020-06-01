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
        def source = "src/main/cd/deploy-${params.ENV_LABEL}.yml"
        sh"sed -e 's#{TAG}#${buildId}#g' ${source} > deployment.yml"
        sh "kubectl apply -f deployment.yml"
    }
}