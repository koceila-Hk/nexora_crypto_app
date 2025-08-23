pipeline {
    agent any

    environment {
        DOCKERHUB_CREDENTIALS = credentials('dockerhub-credentials')
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'develop', url: 'https://github.com/koceila-Hk/nexora_crypto_app.git'
            }
        }

        stage('Build Backend') {
            steps {
                script {
                    backendImage = docker.build("kousshk/nexora-backend:${env.BUILD_NUMBER}", "./nexora-crypto-api")
                }
            }
        }

        stage('Build Frontend') {
            steps {
                script {
                    frontendImage = docker.build("kousshk/nexora-frontend:${env.BUILD_NUMBER}", "./nexora-crypto-dashboard/webapps/dashboard")
                }
            }
        }

        stage('Check credentials') {
            steps {
                script {
                    echo "DOCKERHUB_CREDENTIALS: ${env.DOCKERHUB_CREDENTIALS}"
                }
            }
        }

        stage('Push Images') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', DOCKERHUB_CREDENTIALS) {
                        backendImage.push()
                        backendImage.push("latest")

                        frontendImage.push()
                        frontendImage.push("latest")
                    }
                }
            }
        }
    }
}
