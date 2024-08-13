pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                // Clone the repository
                git branch: 'main', url: 'https://github.com/sathamhussain123/sample-todo-android-app.git'
            }
        }

        stage('Build') {
            steps {
                // Give execution permission to the gradlew script
                sh 'chmod +x ./gradlew'
                
                // Build the Android app
                sh './gradlew assembleDebug'
            }
        }
    }

    post {
        always {
            // Clean up the workspace after the build
            cleanWs()
        }
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
