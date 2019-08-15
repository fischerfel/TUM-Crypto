public String encrypt(String s,byte[] d) throws Exception {

    // Perform Encryption
    SecretKeySpec eks = new SecretKeySpec(d, "AES");
    Cipher c = Cipher.getInstance("AES/CTR/NoPadding");
    c.init(Cipher.ENCRYPT_MODE, eks, new IvParameterSpec(new byte[16]));
    byte[] es = c.doFinal(s.getBytes(StandardCharsets.UTF_8));
}
