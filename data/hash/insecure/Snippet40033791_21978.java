import java.security.MessageDigest

def hashString(String s){
    MessageDigest.getInstance("SHA1").digest(s.bytes).encodeHex().toString()
}

allprojects {
    buildDir = "C:/AB/${hashString(projectDir.getAbsolutePath())}"
}
