object Encoder {
  def sha1(s: Row): String = MessageDigest.getInstance("SHA-1").digest(s.mkString.getBytes()).toString
}
