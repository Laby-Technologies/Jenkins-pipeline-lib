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
        sh "docker load -i ${props.path}"
        return true
    } catch (Exception e) {
        currentBuild.result = 'UNSTABLE' // Warning, continues
        error(props.pathImage)
        return false
    }
}

