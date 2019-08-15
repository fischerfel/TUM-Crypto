import java.security.MessageDigest

public static String generateDigest(File file, String digest, int paddedLength){
    MessageDigest md = MessageDigest.getInstance(digest)
    md.reset()
    def files = []
    def directories = []

    if(file.isDirectory()){
        file.eachFileRecurse(){sf ->
            if(sf.isFile()){
                files.add(sf)
            }
            else{
                directories.add(file.toURI().relativize(sf.toURI()).toString())
            }
        }
    }
    else if(file.isFile()){
        files.add(file)
    }

    files.sort({a, b -> return a.getAbsolutePath() <=> b.getAbsolutePath()})
    directories.sort()

    files.each(){f ->
        println file.toURI().relativize(f.toURI()).toString()
        f.withInputStream(){is ->
            byte[] buffer = new byte[8192]
            int read = 0
            while((read = is.read(buffer)) > 0){
                md.update(buffer, 0, read)
            }
        }
    }

    directories.each(){d ->
        println d
        md.update(d.getBytes())
    }

    byte[] digestBytes = md.digest()
    BigInteger bigInt = new BigInteger(1, digestBytes)
    return bigInt.toString(16).padLeft(paddedLength, '0')
}

println "\n${generateDigest(new File(args[0]), 'SHA-256', 64)}"
