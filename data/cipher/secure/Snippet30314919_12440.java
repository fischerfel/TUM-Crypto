def encrypt(iv4bytes: Array[Byte], pass: String, indata: Array[Byte]): Array[Byte] = {
    val cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE") // Get a cipher object
    val key = new SecretKeySpec(pass.getBytes("UTF-8"), "AES") // Get our key object
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv4bytes)) // Initialize crypto
    return cipher.doFinal(indata) // And do the encryption
  }
