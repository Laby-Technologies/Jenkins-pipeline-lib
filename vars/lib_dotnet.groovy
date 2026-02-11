def lib_docker

def initialize() {
    this.lib_docker = library('lib_docker').lib_docker
}

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

def call() {
    if (!this.lib_docker) {
        initialize()
    }
}
