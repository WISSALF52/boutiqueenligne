pipeline {
    agent any

    stages {
        stage('Checkout') {
            steps {
                script {
                    // Cloner le repository depuis Git
                    checkout scm
                }
            }
        }

        stage('Compile') {
            steps {
                script {
                    // Compiler les fichiers Java sous Windows
                    bat '''
                    mkdir out 2>nul
                    for /r %%i in (*.java) do javac -d out %%i
                    '''
                }
            }
        }

        stage('Run') {
            steps {
                script {
                    // Exécuter le programme principal
                    bat 'java -cp out BoutiqueEnLigne'
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    def mvn = tool name: 'Default Maven', type: 'maven'
                    withSonarQubeEnv() {
                        bat "\"${mvn}\\bin\\mvn\" clean verify sonar:sonar -Dsonar.projectKey=BOU -Dsonar.projectName=\"BOU\""
                    }
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline terminé avec succès !'
        }
        failure {
            echo 'Une erreur est survenue.'
        }
    }
}
