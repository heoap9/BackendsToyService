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
        STATIC_RESOURCES_DIR = 'src/main/resources'
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
                // 빌드된 JAR 파일과 정적 리소스 파일을 원격 서버로 전송
                sshagent(credentials: [SSH_CREDENTIALS_ID]) {
                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} "pwd; ls -l ${REMOTE_DIR}"'
                    sh 'scp -o StrictHostKeyChecking=no build/libs/${JAR_NAME} ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}'

                    // 정적 리소스 디렉토리를 원격 서버로 전송
                    sh 'scp -o StrictHostKeyChecking=no -r ${STATIC_RESOURCES_DIR}/templates ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/resources/'
                    sh 'scp -o StrictHostKeyChecking=no -r ${STATIC_RESOURCES_DIR}/static ${REMOTE_USER}@${REMOTE_HOST}:${REMOTE_DIR}/resources/'

                    sh 'ssh -o StrictHostKeyChecking=no ${REMOTE_USER}@${REMOTE_HOST} pkill -f "java -jar ${REMOTE_DIR}/${JAR_NAME}" || true'
                    // 새로운 JAR 파일 실행 명령어 추가
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
