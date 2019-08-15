try {

        String encryptionKey = "1234567890123456";
        String plaintext = "1234567890123456";

        System.out.println("key:   " + encryptionKey);
        System.out.println("plain:   " + plaintext);
        byte[] a = aes.encrypt(plaintext, encryptionKey);
        String b = new String(a);
        byte[] c = b.getBytes();

        String decrypted = new String(aes.decrypt(c, encryptionKey));

        System.out.println("decrypt: " + decrypted);

    } catch (Exception e) {
      e.printStackTrace();
    } 
}


public byte[] encrypt(String plainText, String encryptionKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes(), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes()));
    return cipher.doFinal(plainText.getBytes());
}
