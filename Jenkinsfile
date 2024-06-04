pipeline {
    agent any
    tools {
        jdk 'jdk' // Jenkins에서 설정한 JDK 이름
    }

    environment {
        REMOTE_USER = 'root'
        REMOTE_HOST = '192.168.0.15'
        REMOTE_DIR = '/root/spring-app'
        SSH_CREDENTIALS_ID = 'jenkins'
        JAR_NAME = 'BackendsService-0.0.1-SNAPSHOT.jar'
        BUILD_DIR = 'build'
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github_Token', url: 'https://github.com/heoap9/BackendsToyService'
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build'
            }
        }

        stage('Deploy') {
            steps {
                sshagent(credentials: [SSH_CREDENTIALS_ID]) {
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "mkdir -p ${REMOTE_DIR}"'
                    sh 'rsync -avz -e "ssh -o StrictHostKeyChecking=no" ${BUILD_DIR}/ ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/'
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "pgrep -f \'java -jar ${REMOTE_DIR}/libs/${JAR_NAME}\' | xargs --no-run-if-empty kill" || true'
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "nohup java -jar ${REMOTE_DIR}/libs/${JAR_NAME} > ${REMOTE_DIR}/app.log 2>&1 &"'
                }
            }
        }

        stage('Show Logs') {
            steps {
                sshagent(credentials: [SSH_CREDENTIALS_ID]) {
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "tail -n 100 ${REMOTE_DIR}/app.log"'
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                sshagent(credentials: [SSH_CREDENTIALS_ID]) {
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "ls -R ${REMOTE_DIR}"'
                }
            }
        }

        stage('Server Monitoring') {
            steps {
                sshagent(credentials: [SSH_CREDENTIALS_ID]) {
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "top -b -n 1 | head -n 20"'
                }
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
