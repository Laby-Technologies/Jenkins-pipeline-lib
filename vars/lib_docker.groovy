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
        def imageNameLower = props.imageName.toLowerCase()
        def sanitizedTag = props.currentTag.replaceFirst(/^v/, '')
        sh "docker tag ${imageNameLower}:${sanitizedTag} ${imageNameLower}:latest"
        return true
    } catch (Exception e) {
        currentBuild.result = 'UNSTABLE' // Warning, continues
        error("Failed to tag ${props.imageName}:${sanitizedTag} as latest")
        return false
    }
}


