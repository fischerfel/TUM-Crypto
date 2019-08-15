def SHA1 = hash(MessageDigest.getInstance("SHA-1"))_
def MD5 = hash(MessageDigest.getInstance("MD5"))_
private def hash(algorithm:HashAlgorithm)(s:String, urlencode:Boolean = false) = {
    val form = if (urlencode) "%%%02X" else "%02X"
    (algorithm.digest(s.getBytes) map(form format _)).mkString
}
