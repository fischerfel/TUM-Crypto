task printDependencyHashes() {
    def hash = { File file ->
        def md = java.security.MessageDigest.getInstance('MD5')
        file.eachByte(1024 * 4) { buffer, len ->
            md.update(buffer, 0, len)
        }
        return md.digest().encodeHex().toString()
    }

    doLast {
        configurations.compileClasspath.each { println "${it.name}: ${hash(it)}" }
    }
}
