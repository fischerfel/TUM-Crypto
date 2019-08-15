import java.security.MessageDigest

val digest = MessageDigest.getInstance("MD5")

//Quick MD5 of text
val text = "MD5 this text!"
val md5hash1 = digest.digest(text.getBytes).map("%02x".format(_)).mkString

//MD5 of text with updates
digest.update("MD5 ".getBytes())
digest.update("this ".getBytes())
digest.update("text!".getBytes())
val md5hash2 = digest.digest().map(0xFF & _).map("%02x".format(_)).mkString

//Output
println(md5hash1 + " should be the same as " + md5hash2)
