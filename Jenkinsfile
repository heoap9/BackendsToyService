pipeline {
    agent any

    tools {
        jdk 'JDK 17' // Jenkins에서 설정한 JDK 이름
    }

    environment {
        // 환경 변수 설정
        REMOTE_USER = 'root'
        REMOTE_HOST = '192.168.0.15'
        REMOTE_DIR = '/root/spring-app'
        SSH_KEY = 'github_Token'
        JAR_NAME = 'spring.jar'
    }

    stages {
        stage('Checkout') {
            steps {
                // Git 리포지토리에서 소스 코드 체크아웃
                git
                    branch: 'master',
                    url: 'https://github.com/heoap9/BackendsToyService'
            }

            post {
                success {
                    sh 'echo "Success clone Repo"'
                    }
                failure {
                    sh 'echo "fail..clone"'
                    }
            }
        }

        stage('Build') {
            steps {
                // Gradle을 사용하여 프로젝트 빌드
                sh './gradlew clean build'
            }

            post {
                            success {
                                sh 'echo "Success build "'
                                }
                            failure {
                                sh 'echo "fail..build"'
                                }
                        }
        }

        stage('Deploy') {
                    steps {
                        // 빌드된 JAR 파일을 원격 서버로 전송
                        sshagent(credentials: [env.SSH_KEY]) {
                            sh """
                            scp build/libs/${env.JAR_NAME} ${env.REMOTE_USER}@${env.REMOTE_HOST}:${env.REMOTE_DIR}
                            ssh ${env.REMOTE_USER}@${env.REMOTE_HOST} 'bash -s' <<'ENDSSH'
                            # 서버에서 기존 프로세스를 종료하고 새 JAR 파일로 애플리케이션을 시작
                            pkill -f 'java -jar ${env.REMOTE_DIR}/${env.JAR_NAME}' || true
                            nohup $JAVA_HOME/bin/java -jar ${env.REMOTE_DIR}/${env.JAR_NAME} > /dev/null 2>&1 &
                            ENDSSH
                            """
                        }
                    }
        }
    }

    stage('Health Check') {
                steps {
                    script {
                        def response = httpRequest url: 'http://192.168.0.15:8080/health', validResponseCodes: '200'
                        if (response.status != 200) {
                            error "Health check failed with status: ${response.status}"
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

