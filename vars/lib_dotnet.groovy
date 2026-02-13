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
