val aKey = generateAESKey()

val kG = KeyPairGenerator.getInstance("RSA")
kG.initialize(2048)
val own = kG.genKeyPair()
val strange = kG.genKeyPair()

String(aKey.encoded).encryptRSA(strange.public).encryptRSA(own.public)

fun generateAESKey(): Key {
val generator = KeyGenerator.getInstance("AES")
generator.init(128)
return generator.generateKey()

fun String.encryptRSA(key: Key): String {
    val encryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    encryptCipher.init(Cipher.ENCRYPT_MODE, key)
    val cipherText = encryptCipher.doFinal(this.toByteArray(charset("UTF-
    8")))
    return String(cipherText)
}

fun String.decryptRSA(key: Key): String {
    val bytes = this.toByteArray()
    val decryptCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding")
    decryptCipher.init(Cipher.DECRYPT_MODE, key)
    return String(decryptCipher.doFinal(bytes), charset("UTF-8"))
}
