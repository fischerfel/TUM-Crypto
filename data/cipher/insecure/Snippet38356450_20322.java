val encrypted = encrypt(zipped.toString)

def encrypt(value: String): String = {
  val cipher: Cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
  cipher.init(Cipher.ENCRYPT_MODE, keyToSpec(encryptionPassword))
  Base64.encodeBase64String(cipher.doFinal(value.getBytes("UTF-8")))
}
