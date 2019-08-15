private String encrypt(String message) {
    SecretKey outKey = // key from session
    byte[] iv = // iv from session
    Cipher outCipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    outCipher.init(Cipher.ENCRYPT_MODE, outKey, new IvParameterSpec(iv))
    return Hex.encodeToString(outCipher.doFinal(message.bytes)).trim()
}
