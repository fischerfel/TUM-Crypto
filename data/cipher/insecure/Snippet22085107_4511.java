  val secretKey = new SecretKeySpec("some key".getBytes("UTF-8"), "DES")
  val encipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
  encipher.init(Cipher.ENCRYPT_MODE, secretKey)
  val encrypted = encipher.doFinal(bytes)

  println("BYTES:" + bytes)
  println("ENCRYPTED!!!!!!: " + encrypted)
  println(toString(encrypted))
