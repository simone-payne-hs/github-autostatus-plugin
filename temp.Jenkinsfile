pipeline {
    agent {
        kubernetes {
            label 'maven'
            inheritFrom 'jenkins-slave-small'
            containerTemplate {
                name 'maven'
                image 'maven:3.3.9-jdk-8-alpine'
                ttyEnabled true
                command 'cat'
            }
        }
    }
    stages {
        stage('Build') {
            steps {
                container(name: 'maven', shell: '/bin/bash') {
                    sh 'mvn install'
                }
            }
        }
        stage('Deploy') {
            steps {
                container(name: 'maven', shell: '/bin/bash') {
                    sh 'mvn deploy'
                }
            }
        }
    }
}