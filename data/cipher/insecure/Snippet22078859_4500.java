val secretKey = new SecretKeySpec("some key".getBytes("UTF-8"), "DES")
val encipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
encipher.init(Cipher.ENCRYPT_MODE, secretKey)
val encrypted = encipher.doFinal("hello world".getBytes)
println(encrypted) // prints: [B@4896ceb3
println(java.util.Arrays.toString(encrypted)) // [-45, -126, -90, 36, 8, -73, 6, 85, -94, 108, 100, -120, 15, -8, 126, 76]
println(Hex.encodeHexString(encrypted)) //prints: 822c90f1116686e75160ff06c8faf4a4
