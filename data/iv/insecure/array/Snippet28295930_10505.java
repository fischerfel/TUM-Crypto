static byte[] IV = {  0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f };

static String plaintext = "00010101010102020202020303030303"; 

static byte[] encryptionKey = {0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41,
    0x41, 0x41, 0x41, 0x41, 0x41, 0x41, 0x41  };

public static void main(String[] args) {
    try {

        System.out.println("==Java==");
        System.out.println("plain:   " + plaintext);

        byte[] cipher = encrypt(plaintext, encryptionKey);

        System.out.print("cipher:  ");
        for (int i = 0; i < cipher.length; i++) {
            // System.out.print(new Integer(cipher[i]) + " ");
            byte b = cipher[i];
            System.out.print(String.format("%02x", b & 0xFF) + " ");

        }

        String decrypted = decrypt(cipher, encryptionKey);

        System.out.println("decrypt: " + decrypted);

    } catch (Exception e) {
        e.printStackTrace();
    }
}

public static byte[] encrypt(String plainText, byte[] encryptionKey)
        throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV));
    return cipher.doFinal(plainText.getBytes("UTF-8"));
}

public static String decrypt(byte[] cipherText, byte[] encryptionKey)
        throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec key = new SecretKeySpec(encryptionKey, "AES");
    cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(IV));
    return new String(cipher.doFinal(cipherText), "UTF-8");
}
