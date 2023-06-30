node("ci-node") {

    stage("checkout") {
        checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mchekini-check-consulting/staff-manager-api']])
    }

    stage("build") {
        sh "chmod 777 mvnw"
        sh "./mvnw clean package -DskipTests=true"
    }

    stage("build image") {
        sh "sudo docker build -t staff-manager-api ."
    }

    stage("push docker image") {

        withCredentials([usernamePassword(credentialsId: 'mchekini', usernameVariable: 'username',
                passwordVariable: 'password')]) {
            sh "sudo docker login -u mchekini -p $password"
            sh "sudo docker tag staff-manager-api mchekini/staff-manager-api:1.0"
            sh "sudo docker push mchekini/staff-manager-api:1.0"
            stash includes: 'docker-compose.yml', name: 'utils'
        }
    }

    node("apps-integration"){
        stage("deploy"){
            unstash 'utils'
            try{
                sh "sudo docker-compose down"
                sh "sudo docker-compose pull"
                sh "sudo docker-compose up -d"

            }catch (Exception e){
                println "No Running Docker Compose Running"
                sh "sudo docker-compose pull"
                sh "sudo docker-compose up -d"
            }

        }
    }


}