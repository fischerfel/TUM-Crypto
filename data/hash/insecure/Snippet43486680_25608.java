import java.security.MessageDigest

def getMD5Hash(fileName: String): String = {
    val msgDigest:MessageDigest = MessageDigest.getInstance("MD5")
    val MD5Hash = msgDigest.digest(fileName.getBytes()).map(0xFF & _).map { "%02x".format(_) }.foldLeft("") {_ + _}
    MD5Hash
}
