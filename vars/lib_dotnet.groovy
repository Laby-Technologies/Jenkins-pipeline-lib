def dotnetTest(Map props = [:]) {
    def COMMAND = "dotnet test"
    // docker exec labycarweb-dotnet dotnet build file.sln ...
    return "docker exec ${props.nameContainer} ${COMMAND} \"${props.pathSolution}\" ${props.params}"
}

//  q[uiet], m[inimal], n[ormal], d[etailed], and diag[nostic]
def dotnetBuild(Map props = [:]) {
    def verbosity = props.verbosity ?: 'n'
    def COMMAND = "dotnet build -v ${verbosity}"
    // docker exec labycarweb-dotnet dotnet build file.sln ...
    return "docker exec ${props.nameContainer} ${COMMAND} \"${props.pathSolution}\" ${props.params}"
}


/* 
    nameContainer   : labycar-dotnet
    folderImages    : /var/images
    projectFolder   : ./WebAPI/WebAPI.csproj    
    imageTag        : 0.1.0    
    imageName       : name of image    
 */
def dotnetPublish(Map props = [:]) {
    def COMMAND = "dotnet publish"
    def PARAMS = """ \
        --os linux --arch x64 /t:PublishContainer \
        -p ContainerArchiveOutputPath="${props.folderImages}/${props.imageName}.tar.gz" \
        -p ContainerRepository=${props.imageName} \
        -p ContainerImageTag=${props.imageTag} \
        """
    // docker exec labycarweb-dotnet dotnet build file.sln ...
    return "docker exec ${props.nameContainer} ${COMMAND} ${props.projectFolder} ${PARAMS}"
}
