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
                    bat 'javac -d out $(for /r %i in (*.java) do @echo %i)'
                }
            }
        }

        stage('Run') {
            steps {
                script {
                    // Exécuter le programme principal sous Windows
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
