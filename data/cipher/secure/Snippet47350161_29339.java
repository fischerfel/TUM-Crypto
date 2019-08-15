val sPublic = "PUBLIC KEY HERE"

fun encrypt(clearText: String, publicKey: String): String {

    var encryptedBase64 = ""

    try {
        val keyFac = KeyFactory.getInstance("RSA")
        val keySpec = X509EncodedKeySpec(Base64.decode(publicKey.toByteArray(), Base64.DEFAULT))
        val key = keyFac.generatePublic(keySpec)

        // get an RSA cipher object
        val cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING")
        // Initiate the cypher mode and declare what key to use (PUBLIC KEY)
        cipher.init(Cipher.ENCRYPT_MODE, key)

        // Encrypt the clearText
        val encryptedBytes = cipher.doFinal(clearText.toByteArray(charset("UTF-8")))
        // Turn it into a base64 string
        encryptedBase64 = String(Base64.encode(encryptedBytes, Base64.DEFAULT))

    } catch (e: Exception) {
        e.printStackTrace()
    }

    // Replace the newlines
    return encryptedBase64.replace("([\\r\\n])".toRegex(), "")
}

// The encryptedText variable gets sent to the server
val encryptedText = encrypt("This is a secret", s.Public)
