val cipher = Cipher.getInstance("AES/CBC/PKCS7Padding")
val key = keystore.getAlias("MyKey", null)

val iv = ByteArray(16)
val random = SecureRandom()
random.nextBytes(iv)

cipher.init(Cipher.ENCRYPT_MODE, key, IvParameterSpec(iv))
