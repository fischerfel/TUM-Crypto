val algorithmName = "TripleDES"
def encrypt(bytes: Array[Byte], secret: String): Array[Byte] = {
  val secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), algorithmName)
  val encipher = Cipher.getInstance(algorithmName + "/CBC/PKCS5Padding")
  val iv = encipher.getParameters.getParameterSpec(classOf[IvParameterSpec])
  encipher.init(Cipher.ENCRYPT_MODE, secretKey, iv)
  encipher.doFinal(bytes) ++ iv.getIV
}

def decrypt(bytes: Array[Byte], secret: String): Array[Byte] = {
  val secretKey = new SecretKeySpec(secret.getBytes("UTF-8"), algorithmName)
  val encipher = Cipher.getInstance(algorithmName + "/CBC/PKCS5Padding")
  val ivIndex = bytes.length - 8
  val iv = new IvParameterSpec(bytes, ivIndex, 8)
  encipher.init(Cipher.DECRYPT_MODE, secretKey, iv)
  encipher.doFinal(bytes, 0, ivIndex)
}
