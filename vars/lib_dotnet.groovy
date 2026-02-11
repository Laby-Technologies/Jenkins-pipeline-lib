@Library("lib") _

def dotnetTest(Map props = [:]) {
    def COMMAND = "dotnet test"
    // docker exec labycarweb-dotnet dotnet build file.sln ...
    return lib_docker.dockerExec(props.nameContainer, "$COMMAND \"${props.pathSolution}\" ${props.params}")
}

//  q[uiet], m[inimal], n[ormal], d[etailed], and diag[nostic]
def dotnetBuild(Map props = [:]) {
    def COMMAND = "dotnet build -v ${props.verbosity}"
    // docker exec labycarweb-dotnet dotnet build file.sln ...
    return lib_docker.dockerExec(props.nameContainer, "$COMMAND \"${props.pathSolution}\" ${props.params}")
}
