 import java.security.MessageDigest

 def hash(s: String) = {
    MessageDigest.getInstance("MD5").digest(s.sorted.getBytes)
 }
