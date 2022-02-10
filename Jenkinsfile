pipeline {
  agent any
  options {
    disableConcurrentBuilds()
  }
  tools {
    gradle 'gradle-7.3.3'
    jdk 'jdk-16.0.1'
  }
  environment {
    SONATYPE_CREDS = credentials('sonatype')
  }
  stages {
    stage('build') {
      steps {
        sh('gradle clean build')
      }
    }
    stage('publish') {
      steps {
        sh('gradle publish closeAndReleaseRepository')
      }
    }
  }
}
