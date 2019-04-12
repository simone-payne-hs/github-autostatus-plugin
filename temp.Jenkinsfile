pipeline {
    agent {
        kubernetes {
            label 'maven'
            inheritFrom 'jenkins-slave'
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
                    // We need to move the settings.xml over for it to work.
                    sh 'mv /home/jenkins/.m2/settings.xml /root/.m2/settings.xml'
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