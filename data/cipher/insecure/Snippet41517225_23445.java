@Test
public void testCipherGetInstanceShouldDefaultToECB() throws Exception {
    // Arrange
    final String PLAINTEXT = "This is a plaintext message."
    final SecretKey key = new SecretKeySpec(Hex.decodeHex("0123456789ABCDEFFEDCBA9876543210" as char[]), "AES")

    Cipher unspecified = Cipher.getInstance("AES")
    final Cipher EXPECTED_CIPHER = Cipher.getInstance("AES/ECB/PKCS5Padding")

    unspecified.init(Cipher.ENCRYPT_MODE, key)
    EXPECTED_CIPHER.init(Cipher.DECRYPT_MODE, key)

    // Act
    byte[] cipherBytes = unspecified.doFinal(PLAINTEXT.getBytes(StandardCharsets.UTF_8))
    logger.info("Cipher text: ${Hex.encodeHexString(cipherBytes)}")

    // Assert
    byte[] recoveredBytes = EXPECTED_CIPHER.doFinal(cipherBytes)
    String recovered = new String(recoveredBytes, StandardCharsets.UTF_8)
    assert recovered == PLAINTEXT
}
