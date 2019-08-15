private def getMd5(inputStr: String): String = {
  val md: MessageDigest = MessageDigest.getInstance("MD5")
  md.digest(inputStr.getBytes()).map(0xFF & _).map { "%02x".format(_) }.foldLeft("") {_ + _}
}
