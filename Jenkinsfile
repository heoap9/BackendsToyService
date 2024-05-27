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
        JAR_NAME = 'BackendsService-0.0.1-SNAPSHOT.jar' // 수정된 JAR 파일 이름
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
                sh './gradlew build'
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
                // 빌드된 JAR 파일을 원격 서버로 전송
                sshagent(credentials: ['jenkins']) {
                    sh 'ssh -o StrictHostKeyChecking=no root@192.168.0.15 "pwd; ls -l /root/spring-app"'
                    sh 'scp -o StrictHostKeyChecking=no build/libs/BackendsService-0.0.1-SNAPSHOT.jar root@192.168.0.15:/root/spring-app'
                    sh 'ssh -o StrictHostKeyChecking=no root@192.168.0.15 pkill -f \'java -jar /root/spring-app/BackendsService-0.0.1-SNAPSHOT.jar\' || true'
                    // 새로운 JAR 파일 실행 명령어 추가
                    sh 'ssh -o StrictHostKeyChecking=no root@192.168.0.15 "nohup java -jar /root/spring-app/BackendsService-0.0.1-SNAPSHOT.jar > /root/spring-app/app.log 2>&1 &"'
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

        stage('Health Check') {
            steps {
                script {
                    def response = httpRequest url: 'http://192.168.0.14:8200/health', validResponseCodes: '200'
                    if (response.status != 200) {
                        error "Health check failed with status: ${response.status}"
                    } else {
                        sh 'echo "Health check passed"'
                    }
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
