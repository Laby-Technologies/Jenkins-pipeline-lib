// Define functions at the top level
def dockerExec(Map props = [:]) {
    return "docker exec ${props.nameContainer} ${props.params}"
}

def dockerStop(Map props = [:]){
    try {
        sh "docker stop ${props.nameContainer}"
        return true
    } catch (Exception e) {
        currentBuild.result = 'ABORTED' // Marks build as aborted
        error(props.nameContainer)
        return false
    }
}

def dockerStart(Map props = [:]){
    try {
        sh "docker start ${props.nameContainer}"
        return true
    } catch (Exception e) {
        currentBuild.result = 'UNSTABLE' // Warning, continues
        error(props.nameContainer)
        return false
    }
}

/* 
    pathImage       : /WebAPI/v0.2.0/WebAPI.tar.gz

    es: docker load -i /WebAPI/v0.2.0/WebAPI.tar.gz
 */
def dockerLoad(Map props = [:]){
    try {
        sh "docker load -i ${props.pathImage}"
        return true
    } catch (Exception e) {
        currentBuild.result = 'ABORTED' // Warning, continues
        error(props.pathImage)
        return false
    }
}


/* 
    filterName       : web
    es: docker images --tree -f "reference=web*" 
 */
def dockerImageLs(Map props = [:]){
    String filter = new String(props.filterName).toLowerCase()
    try {
        sh "docker images -f reference=${filter}"
        return true
    } catch (Exception e) {
        currentBuild.result = 'UNSTABLE' // Warning, continues
        error(props.filterName)
        return false
    }
}


/*
    imageName    : WebAPI
    currentTag   : v0.2.0
    latestTag    : latest
    es: docker tag WebAPI:v0.2.0 WebAPI:latest
*/
def dockerTagLatest(Map props = [:]) {
    try {
        def imageNameLower = props.imageName.toLowerCase().replace('.', '-')
        def sanitizedTag = props.currentTag.replaceFirst(/^v/, '')
        sh "docker tag ${imageNameLower}:${sanitizedTag} ${imageNameLower}:latest"
        return true
    } catch (Exception e) {
        currentBuild.result = 'UNSTABLE' // Warning, continues
        error("Failed to tag ${props.imageName}:${sanitizedTag} as latest")
        return false
    }
}

def dockerComposeUp(){
  try{
    sh "docker compose up "
    return true
  } catch(Exception e){
      currentBuild.result = 'UNSTABLE' // Warning, continues
      error("Failed to launch docker compose up")
      return false
  }
}

/*
    Esegue login al registry, tagga l'immagine locale con il tag di versione
    e con latest, pusha entrambi i tag e infine esegue il logout.
    Le credenziali sono lette da Jenkins tramite withCredentials.

    credentialsId  : registry-creds                          (obbligatorio, credenziale Jenkins di tipo username/password)
    registryUrl    : registry.example.com
    localImage     : webapi:0.2.0
    remoteVersion  : registry.example.com/team/webapi:0.2.0
    remoteLatest   : registry.example.com/team/webapi:latest

    es: docker login registry.example.com -u $REGISTRY_USER --password-stdin
        docker tag webapi:0.2.0 registry.example.com/team/webapi:0.2.0
        docker tag webapi:0.2.0 registry.example.com/team/webapi:latest
        docker push registry.example.com/team/webapi:0.2.0
        docker push registry.example.com/team/webapi:latest
        docker logout registry.example.com
*/
def dockerPushRepository(Map props =[:]){
    String credsId = props.credentialsId ?: error("credentialsId è obbligatorio")
    try{
        withCredentials([usernamePassword(
            credentialsId: credsId,
            usernameVariable: 'REGISTRY_USER',
            passwordVariable: 'REGISTRY_PASS'
        )]) {
            sh """
                echo "\$REGISTRY_PASS" | docker login ${props.registryUrl} -u "\$REGISTRY_USER" --password-stdin
                docker tag ${props.localImage} ${props.remoteVersion}
                docker push ${props.remoteVersion}
                docker logout ${props.registryUrl}
            """
        }
    } catch(Exception e){
           currentBuild.result = 'UNSTABLE' // Warning, continues
           error("Failed to publish on private repository")
           return false
       }
}

