node {
    properties([
            parameters([
                    string(defaultValue: 'beta', description: 'image tag postfix', name: 'imageTagPostfix',
                            trim: false)
            ])
    ])
    stage("tag image") {
        def imageTag = "loeyae/springboot_demo:${params.imageTagPostfix}"
        withCredentials([dockerCert(credentialsId: 'docker-local', variable: 'DOCKER_CERT_PATH')]) {
            try {
                sh """
                  docker tag springboot_demo:latest $imageTag
                  docker push $imageTag
                  """
            }
            catch (exc) {
                println("Push image failure")
                print(exc.getMessage())
                currentBuild.result = 'FAILURE'
            }
        }
    }
}