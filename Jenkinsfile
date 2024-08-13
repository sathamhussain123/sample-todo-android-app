pipeline {
    agent any

   // environment {
        //GRADLE_HOME = '/path/to/gradle'  // Update this to the correct path if not in PATH
        //ANDROID_HOME = '/path/to/android/sdk'  // Path to Android SDK
        //PATH = "$GRADLE_HOME/bin:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools:$PATH"
   // }

    stages {
        stage('Clone Repository') {
            steps {
                // Clone the repository
                git branch: 'main', url: 'https://github.com/sathamhussain123/sample-todo-android-app.git'
            }
        }

        //stage('Set Up Gradle Wrapper') {
          //  steps {
                // Set up Gradle wrapper
            //    sh 'gradle wrapper'
              //  sh './gradlew wrapper --gradle-version 7.5'
            //}
        //}

        stage('Build') {
            steps {
               // Build the Android app
                sh './gradlew assembleDebug' 
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
}
