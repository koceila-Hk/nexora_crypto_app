pipeline {
    agent any

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

        stage('Push Images') {
            steps {
                script {
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-login') {
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
