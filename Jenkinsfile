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
                    // Compiler les fichiers Java
                    sh 'javac -d out $(find . -name "*.java")'
                }
            }
        }

        stage('Run') {
            steps {
                script {
                    // Exécuter le programme principal
                    sh 'java -cp out BoutiqueEnLigne'
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
