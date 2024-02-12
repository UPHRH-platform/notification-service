pipeline {
    agent any
    
    environment {
        GCR_REGISTRY = "asia.gcr.io/upsmf-368011" 
        IMAGE_NAME = "notification-service-be-uat"
        BRANCH_NAME = "main" 
	IMAGE_TAG = "1.0"   
    }

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'main', url: 'https://github.com/UPHRH-platform/notification-service.git'
            }
        }
     }
}
      
