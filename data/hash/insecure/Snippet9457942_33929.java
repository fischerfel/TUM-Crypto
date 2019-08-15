fun main(args : Array<String>) {
  val md = java.security.MessageDigest.getInstance("SHA")
  if (md == null) throw NullPointerException()
  val result : Array<Byte>? = md.digest() 
}
