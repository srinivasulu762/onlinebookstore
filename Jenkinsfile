pipeline{
agent any

tools
{
maven 'maven3.9.2'
}
parameters {
  choice choices: ['dev', 'test', 'preprod', 'prod'], name: 'branchname'
}

environment{
buildnumber='${BUILD_NUMBER}'
}

stages
{
stage("source code from git")
{
steps
{
git branch: "${params.branchname}", credentialsId: 'githubcred', url: 'https://github.com/srinivasulu762/onlinebookstore.git'
}
}
stage("build")
{
steps
{
sh "mvn clean package"
}
}


stage("build image")
{
steps
{
sh "docker build -t srinivasulukokkinti/maven-standalone-application:${buildnumber} ."
}
}
stage("login dockerhub")
{
steps
{
withCredentials([string(credentialsId: 'password', variable: 'pass')]) {
   sh " docker login -u srinivasulukokkinti -p ${pass}"
}
sh "docker push srinivasulukokkinti/maven-standalone-application:${buildnumber}"
}
}
stage("deploy to production")
{
steps
{
sshagent(['sshkeys']) {
   sh "ssh -o strictHostKeyChecking=no ec2-user@15.206.80.69 docker rm -f mavenapplication || true"
   sh "ssh -o strictHostKeyChecking=no ec2-user@15.206.80.69 docker run -d --name mavenapplication -p 8080:8080 srinivasulukokkinti/maven-standalone-application:${buildnumber}"


}
}
}




}
}
