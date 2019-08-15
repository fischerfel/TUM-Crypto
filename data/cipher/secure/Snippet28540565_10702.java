  def encryptCipher(secret:SecretKeySpec, iv:IvParameterSpec):Cipher = {
    val e = Cipher.getInstance("AES/GCM/NoPadding")
    e.init(Cipher.ENCRYPT_MODE, secret, iv)
  }
