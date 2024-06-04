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
    }

    stages {
        stage('Checkout') {
            steps {
                git credentialsId: 'github_Token', url: 'https://github.com/heoap9/BackendsToyService'
            }
        }

        stage('Deploy to Remote') {
            steps {
                sshagent([SSH_CREDENTIALS_ID]) {
                    // 원격 서버에 소스 코드 전송
                    sh "rsync -avz -e 'ssh -o StrictHostKeyChecking=no' . ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/"

                    // 원격 서버에서 빌드 및 실행
                    sh """
                        ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "
                        cd ${REMOTE_DIR} &&
                        ./gradlew clean build &&
                        pgrep -f 'java -jar ${REMOTE_DIR}/build/libs/BackendsService-0.0.1-SNAPSHOT.jar' | xargs --no-run-if-empty kill || true &&
                        nohup java -jar ${REMOTE_DIR}/build/libs/BackendsService-0.0.1-SNAPSHOT.jar > ${REMOTE_DIR}/app.log 2>&1 &"
                    """
                }
            }
        }

        stage('Show Logs') {
            steps {
                sshagent([SSH_CREDENTIALS_ID]) {
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "tail -n 100 ${REMOTE_DIR}/app.log"'
                }
            }
        }

        stage('Verify Deployment') {
            steps {
                sshagent([SSH_CREDENTIALS_ID]) {
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "ls -R ${REMOTE_DIR}"'
                }
            }
        }

        stage('Server Monitoring') {
            steps {
                sshagent([SSH_CREDENTIALS_ID]) {
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
