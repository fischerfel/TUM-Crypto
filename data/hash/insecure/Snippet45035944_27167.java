def md5 = udf((s: String) => toHex(MessageDigest.getInstance("MD5").digest(s.getBytes("UTF-8"))))
def toHex(bytes: Array[Byte]): String = bytes.map("%02x".format(_)).mkString("")
