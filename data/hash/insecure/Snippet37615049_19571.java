def md5(s: String) = {
    MessageDigest.getInstance("MD5").digest(s.getBytes).map("%02x".format(_)).mkString
}
