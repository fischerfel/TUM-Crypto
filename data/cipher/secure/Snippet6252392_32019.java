public String decrypt(byte[] text) {
    byte[] crypt = null;
    String plainText = null;
    try {
        final Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding", "SunJCE");
        final SecretKey skeySpec = KeyGenerator.getInstance("AES").generateKey();
        cipher.init(Cipher.DECRYPT_MODE, skeySpec);

        crypt = cipher.doFinal(text);
        plainText = new String(crypt);



    } catch (Exception ex) {
          throw new RuntimeException(ex);
    }
        return plainText;
}
