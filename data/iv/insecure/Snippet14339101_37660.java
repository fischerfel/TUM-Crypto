public static void main(String[] args) throws Exception {
    // TODO Auto-generated method stub

    // Dernier exemple CTR mode
    // Clé 16 bits
    byte[] keyBytes = new byte[] { (byte) 0x36, (byte) 0xf1, (byte) 0x83,
            (byte) 0x57, (byte) 0xbe, (byte) 0x4d, (byte) 0xbd,
            (byte) 0x77, (byte) 0xf0, (byte) 0x50, (byte) 0x51,
            (byte) 0x5c, 0x73, (byte) 0xfc, (byte) 0xf9, (byte) 0xf2 };
    // IV 16 bits (préfixe du cipherText)
    byte[] ivBytes = new byte[] { (byte) 0x69, (byte) 0xdd, (byte) 0xa8,
            (byte) 0x45, (byte) 0x5c, (byte) 0x7d, (byte) 0xd4,
            (byte) 0x25, (byte) 0x4b, (byte) 0xf3, (byte) 0x53,
            (byte) 0xb7, (byte) 0x73, (byte) 0x30, (byte) 0x4e, (byte) 0xec };

    // Initialisation
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

    // Mode
    Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");

    String originalText = "hello i am original";
    // ///////////////////////////////ENCRYPTING
    cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
    byte[] ciphered = cipher.doFinal(originalText.getBytes());
    String cipherText = new String(ciphered,"UTF-8");
    System.out.println("ciphered: " + cipherText);
    // ///////////////////////////////DECRYPTING
    cipher = Cipher.getInstance("AES/CTR/NoPadding");

    cipher.**init(Cipher.DECRYPT_MODE**, key, ivSpec);
    byte[] plain = **cipher.doFinal(ciphered);**
    originalText = new String(plain,"UTF-8");
    System.out.println("plaintext: " + originalText);
}
