class ResolveProjectArchiveDependency extends DefaultTask {
    def String archiveFilename = ""
    def String relativeArchivePath = ""
    def String dependencyScope = ""

    @OutputFile
    File outputFile

    @TaskAction
    void resolveArtifact() {
        def arcFile = project.file("dependencies/"+dependencyScope+"/"+archiveFilename)
        def newArcFile = ""

        if(project.hasProperty('environmentSeparated') && project.hasProperty('separatedDependencyRoot')){
            println "Properties set denoting the prerelease environment is separated"
            newArcFile = project.file(project.ext.separatedDependencyRoot+relativeArchivePath+archiveFilename)
        }   else {
            newArcFile = project.file('../../'+relativeArchivePath+archiveFilename)
        }

        if(!newArcFile.isFile()){
            println "Warn: Could not find the latest copy of " + archiveFilename + ".."

            if(!arcFile.isFile()) {
                println "Error: No version of " + archiveFilename + " can be found"
                throw new StopExecutionException(archiveFilename +" missing")
            }
        }

        if(!arcFile.isFile()) {
            println archiveFilename + " jar not in dependencies, pulling from archives"
        } else {
            println archiveFilename + " jar in dependencies. Checking for staleness"

            def oldHash = generateMD5(new File(arcFile.path))
            def newHash = generateMD5(new File(newArcFile.path))

            if(newHash.equals(oldHash)) {
                println "Hashes for the jars match. Not pulling in a copy"
                return
            }
        }

        //Copy the archive
        project.copy {
            println "Copying " + archiveFilename
            from newArcFile
            into "dependencies/"+dependencyScope
        }
    }

    def generateMD5(final file) {
       MessageDigest digest = MessageDigest.getInstance("MD5")
       file.withInputStream(){is->
       byte[] buffer = new byte[8192]
       int read = 0
          while( (read = is.read(buffer)) > 0) {
                 digest.update(buffer, 0, read);
             }
         }
       byte[] md5sum = digest.digest()
       BigInteger bigInt = new BigInteger(1, md5sum)
       return bigInt.toString(16)
    }
}
