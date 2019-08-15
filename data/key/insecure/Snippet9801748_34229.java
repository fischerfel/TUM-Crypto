public static String encryptTest() {
  String cleartext = "test1234test1234";
  String key = "TESTKEYTESTKEY12";
    byte[] raw = key.getBytes();
    SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
    try {
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
        byte[] encrypted;
        encrypted = cipher.doFinal(cleartext.getBytes());
    return new String(Base64.encode(encrypted,Base64.DEFAULT));
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
