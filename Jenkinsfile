pipeline {
    agent any
    tools {
        jdk 'jdk' // Jenkins에서 설정한 JDK 이름
    }

    environment {
        // 환경 변수 설정
        REMOTE_USER = 'root'
        REMOTE_HOST = '192.168.0.15'
        REMOTE_DIR = '/root/spring-app'
        SSH_CREDENTIALS_ID = 'jenkins'
        JAR_NAME = 'BackendsService-0.0.1-SNAPSHOT.jar'
        HTML_SRC_DIR = 'src/main/resources/static' // HTML 파일이 있는 디렉토리
        HTML_DEST_DIR = '/var/www/html' // 원격 서버에서 HTML 파일이 배포될 디렉토리
    }

    stages {
        stage('Checkout') {
            steps {
                // Git 리포지토리에서 소스 코드 체크아웃
                git credentialsId: 'github_Token', url: 'https://github.com/heoap9/BackendsToyService'
            }
            post {
                success {
                    sh 'echo "Success clone Repo"'
                }
                failure {
                    sh 'echo "Fail clone Repo"'
                }
            }
        }

        stage('Build') {
            steps {
                // Gradle을 사용하여 프로젝트 빌드
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build'
            }
            post {
                success {
                    sh 'echo "Success build"'
                    // 빌드된 파일 목록 확인
                    sh 'ls -l build/libs/'
                }
                failure {
                    sh 'echo "Fail build"'
                }
            }
        }

        stage('Deploy') {
            steps {
                // 빌드된 JAR 파일 및 HTML 파일을 원격 서버로 전송
                sshagent(credentials: [SSH_CREDENTIALS_ID]) {
                    // JAR 파일 전송
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "pwd; ls -l ${REMOTE_DIR}"'
                    sh 'scp -o StrictHostKeyChecking=no build/libs/${JAR_NAME} ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}'
                    // HTML 파일 전송
                    sh """
                    ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "mkdir -p ${HTML_DEST_DIR}"
                    scp -o StrictHostKeyChecking=no -r ${HTML_SRC_DIR}/* ${REMOTE_USER}@${REMOTE_HOST}:${HTML_DEST_DIR}
                    """
                    // 기존 프로세스 종료 및 새로운 JAR 파일 실행
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} pkill -f "java -jar ${REMOTE_DIR}/${JAR_NAME}" || true'
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "nohup java -jar ${REMOTE_DIR}/${JAR_NAME} > ${REMOTE_DIR}/app.log 2>&1 &"'
                }
            }
            post {
                success {
                    sh 'echo "Success deploy"'
                }
                failure {
                    sh 'echo "Fail deploy"'
                }
            }
        }
    }

    post {
        always {
            // 빌드 결과를 정리
            cleanWs()
        }
    }
}
