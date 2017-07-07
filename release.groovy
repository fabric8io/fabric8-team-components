#!/usr/bin/groovy
def imagesBuiltByPipeline() {
  return []
}

def externalImages(){
  return ['jenkins-jnlp-client','jenkins-docker']
}

def repo(){
 return 'fabric8io/fabric8-team-components'
}

def stage(){
  return stageProject{
    project = repo()
    useGitTagForNextVersion = true
    extraImagesToStage = externalImages()
  }
}

def updateDownstreamDependencies(stagedProject) {
  pushPomPropertyChangePR {
    propertyName = 'fabric8-team-components.version'
    projects = [
            'fabric8io/fabric8-maven-dependencies'
    ]
    version = stagedProject[1]
  }
  pushPomPropertyChangePR {
    propertyName = 'fabric8-team-components.version'
    projects = [
            'fabric8io/fabric8-platform',
            'fabric8io/fabric8-online',
            'fabric8io/fabric8-pipeline-library',
            'fabric8io/fabric8-forge'
    ]
    version = stagedProject[1]
  }
}

def release(project){
  releaseProject{
    stagedProject = project
    useGitTagForNextVersion = true
    helmPush = false
    groupId = 'io.fabric8.fabric8-team-components.apps'
    githubOrganisation = 'fabric8io'
    artifactIdToWatchInCentral = 'jenkins-openshift'
    artifactExtensionToWatchInCentral = 'jar'
    promoteToDockerRegistry = 'docker.io'
    dockerOrganisation = 'fabric8'
    imagesToPromoteToDockerHub = imagesBuiltByPipeline()
    extraImagesToTag = externalImages()
  }
}

return this;
