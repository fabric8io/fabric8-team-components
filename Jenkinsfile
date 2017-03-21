#!/usr/bin/groovy
@Library('github.com/fabric8io/fabric8-pipeline-library@master')
def stagedProject
releaseNode {
  checkout scm
  readTrusted 'release.groovy'
  sh "git remote set-url origin git@github.com:fabric8io/fabric8-team-components.git"

  def pipeline = load 'release.groovy'

  stage ('Stage'){
    stagedProject = pipeline.stage()
  }
  
  stage ('Promote'){
    pipeline.release(stagedProject)
  }

  stage ('Update downstream dependencies'){
    pipeline.updateDownstreamDependencies(stagedProject)
  }
}
