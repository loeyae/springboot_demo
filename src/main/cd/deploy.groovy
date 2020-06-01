node {
    properties([
            parameters([
                    string(defaultValue: 'dev', description: '部署环境标志', name: 'ENV_LABEL', trim:
                            false),
                    booleanParam(defaultValue: false, description: '是否必须上传tag的镜像', name: 'PUSH_TAGED_IMAGE')
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
        if (!buildId) {
            buildId = 0
        }
        def imageTag = "hub.bys.cd/loeyae/springboot_demo:${buildId}"
        def latestTag = "hub.bys.cd/loeyae/springboot_demo:latest"
        withCredentials([dockerCert(credentialsId: 'docker-client', variable: 'DOCKER_CERT_PATH')]) {
            try {
                if (buildId != 0) {
                    def prevId = buildId - 1
                    def prevImageTag = "hub.bys.cd/loeyae/springboot_demo:${prevId}"
                    sh """
                    docker rmi $prevImageTag
                    """
                }
            } catch (exc) {
                print(exc.getMessage())
            }
            try {
                sh """
                  docker pull $latestTag
                  docker tag $latestTag $imageTag
                  docker push $imageTag  
                  """
            }
            catch (exc) {
                println("pull image failure")
                print(exc.getMessage())
                currentBuild.result = 'FAILURE'
            }
            try {
                sh """
                docker rmi $imageTag
                docker rmi $latestTag
                """
            } catch (exc) {
                print(exc.getMessage())
            }
        }
        def source = "src/main/cd/deploy-${params.ENV_LABEL}.yml"
        sh "sed -e 's#{TAG}#${buildId}#g' ${source} > deployment.yml"
        sh "kubectl apply -f deployment.yml"
    }
}