node {
    properties([
            parameters([
                    string(defaultValue: 'dev', description: '部署环境标志', name: 'ENV_LABEL', trim:
                            false)
            ])
    ])
    stage("deploy") {
        sh "kubectl apply -f src/main/cd/deploy-${params.ENV_LABEL}.yml"
    }
}