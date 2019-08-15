import java.security.MessageDigest
def md5(s: String) = MessageDigest.getInstance("MD5").digest(s.getBytes).toString()
