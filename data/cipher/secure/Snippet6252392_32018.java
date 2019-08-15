public byte[] crypt() {
    byte[] crypt = null;
    try {
        final Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding", "SunJCE");
        final SecretKey skeySpec = KeyGenerator.getInstance("AES").generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        crypt = cipher.doFinal(new byte[]{0, 1, 2, 3});


    } catch (Exception ex) {
         throw new RuntimeException(ex);
    }
        return crypt;
}
