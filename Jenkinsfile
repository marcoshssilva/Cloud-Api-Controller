@Library('java') _
pipelineSimpleMavenJavaProject('marcoshssilva/cloud-api-controller', 'jdk-17',
        [
          'ENABLE_SONARQUBE_CHECK': 'true',
          'DEPLOY': 'MAVEN',
          'SONARQUBE_PROJECT_KEY': 'cloud-api-controller_main',
          'SONARQUBE_ENV': 'sonarqube',
          'AGENT_EXTRA_LABELS': 'node-builder'
        ])
