node("ci-node") {

    def branchName = env.BRANCH_NAME
    def instancesNumber = 1;
    def branchEnvMapping = new HashMap<String, String>()
    branchEnvMapping.put("develop", "int")
    branchEnvMapping.put("qua", "qua")
    branchEnvMapping.put("master", "prod")

    def commitSHA = null
    def environment = branchEnvMapping.get(branchName)


    stage("checkout") {
        checkout scmGit(branches: [[name: branchName]], extensions: [], userRemoteConfigs: [[url: 'https://github.com/mchekini-check-consulting/staff-manager-api.git']])
        commitSHA = sh(script: "git log -n 1 --pretty=format:'%H'", returnStdout: true)
    }

    stage("Quality Analyses"){

        sh "chmod 700 mvnw && ./mvnw clean compile -Dspring.profiles.active=test sonar:sonar \\\n" +
                "  -Dsonar.projectKey=staff-manager-api \\\n" +
                "  -Dsonar.projectName='staff-manager-api' \\\n" +
                "  -Dsonar.host.url=http://ci.check-consulting.net:11001 \\\n" +
                "  -Dsonar.token=sqp_5cd16e932dce80599cdd184805e41d82fa76dee9"
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
            sh "sudo docker tag staff-manager-api mchekini/staff-manager-api:$branchName"
            sh "sudo docker push mchekini/staff-manager-api:$branchName"
            sh "sudo docker rmi mchekini/staff-manager-api:$branchName"
            sh "sudo docker rmi staff-manager-api"
            stash includes: 'docker-compose.yml', name: 'utils'
        }
    }

    def nodeNamePrefix = environment + "-node-"

    for (int i = 1; i <= instancesNumber; i++) {
        node(nodeNamePrefix + i) {
            stage("deploy-" + environment + "-instance-" + i) {
                unstash 'utils'
                sh "echo TAG=$branchName > .env"
                sh "echo ACTIVE_PROFILE=$environment >> .env"
                try {
                    sh "sudo docker-compose down"
                    sh "sudo docker-compose pull"
                    sh "sudo docker-compose up -d"

                } catch (Exception e) {
                    println "No Docker Compose Running"
                    sh "sudo docker-compose pull"
                    sh "sudo docker-compose up -d"
                }

            }
        }

    }


}