pipeline {
    agent any

    environment {
        ANDROID_HOME = '/Users/apple/Library/Android/sdk' // Replace with the actual path to your Android SDK
        GRADLE_USER_HOME='/Users/apple/softwares/gradle-8.0/bin'
        //PATH = "${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools:${env.PATH}"
        PATH = "/opt/homebrew/opt/openjdk@11/bin:/Users/apple/softwares/gradle-8.0/bin:/Users/apple/Library/Android/sdk/build-tools/33.0.3:$PATH"
    }
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
                sh "./gradlew wrapper --gradle-version 8.0"
                //sh './gradlew assembleDebug --no-daemon --stacktrace'

                sh './gradlew assembleDebug'
            }
        }
    }

    post {
        //always {
            // Clean up the workspace after the build
            //cleanWs()
        //}
        success {
            echo 'Build completed successfully!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
