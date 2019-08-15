  def aes_encrypt(value: String, k: String): Array[Byte] = {
    val cipher = Cipher.getInstance("AES")
    val key = Arrays.copyOf(k.getBytes("UTF-8"), 16)
    val secretKey: SecretKeySpec = new SecretKeySpec(key, 0, key.length, "AES")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    cipher.doFinal(value.getBytes("UTF-8"), 0, value.getBytes("UTF-8").length)
  }
