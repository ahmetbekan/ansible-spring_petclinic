properties([
    parameters([
        string(defaultValue: '', description: 'Enter IP', name: 'node', trim: true)
        ])
    ])

        node {
            stage('Pull Repo') {
                git url: 'https://github.com/ahmetbekan/ansible-spring_petclinic.git'
            }
            withEnv(['ANSIBLE_HOST_KEY_CHECKING=False', 'PETCLINIC_REPO=https://github.com/ikambarov/spring-petclinic.git', 'PETCLINIC_BRANCH=master']) {
                stage("Install Prerequisites"){
                    ansiblePlaybook credentialsId: 'Jenkins-Master', inventory: "${params.remoHost},", playbook: prerequisites.yml
                    }
                stage("Pull repo"){
                    ansiblePlaybook credentialsId: 'Jenkins-Master', inventory: "${params.remoHost},", playbook: "${WORKSPACE}/pull-repo.yml"
                    }
                stage("Install java"){
                    ansiblePlaybook credentialsId: 'Jenkins-Master', inventory: "${params.remoHost},", playbook: "${WORKSPACE}/install-java.yml"
                    }
                stage("Install maven"){
                    ansiblePlaybook credentialsId: 'Jenkins-Master', inventory: "${params.remoHost},", playbook: "${WORKSPACE}/install-mvnw.yml"
                    }
                stage("Start app"){
                    ansiblePlaybook credentialsId: 'Jenkins-Master', inventory: "${params.remoHost},", playbook: "${WORKSPACE}/start-app.yml"
                    }
              
        }
