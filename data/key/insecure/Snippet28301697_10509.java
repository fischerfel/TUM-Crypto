static byte[] IV = {  0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

static byte[] plaintext = { 0x00, 0x01, 0x01, 0x01, 0x01, 0x01, 0x02, 0x02, 0x02, 0x02, 0x02, 0x03, 0x03, 0x03, 0x03, 0x03 }; 

static byte[] encryptionKey = {0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41,
    0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41  };

public static void main(String[] args) {
    try {

        System.out.println("==Java==");
        System.out.println("plain:   " + toJavaHex(plaintext));

        byte[] cipher = encrypt(plaintext, encryptionKey);

        System.out.println("cipher:  " + toJavaHex(cipher));

        String decrypted = toJavaHex(decrypt(cipher, encryptionKey));

        System.out.println("decrypt: " + decrypted);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static String toJavaHex(byte[] data) {
    StringBuilder sb = new StringBuilder(13 * data.length);
    for (int i = 0; i < data.length; i++) {
        if (i != 0) {
            sb.append(", ");
        }
        sb.append(String.format("(byte) 0x%02x", data[i]));
    }
    return sb.toString();
}

public static byte[] encrypt(byte[] plainText, byte[] encryptionKey)
        throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
    return cipher.doFinal(plainText);
}

public static byte[] decrypt(byte[] cipherText, byte[] encryptionKey)
        throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));
    return cipher.doFinal(cipherText);
}
