final static public String ENCRYPT_KEY = "4EBB854BC67649A99376A7B90089CFF1";
final static public String IVKEY = "ECE7D4111337A511F81CBF2E3E42D105";
private static String deCrypt(String key, String initVector, String encrypted) {
    try {
       IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
        SecretKeySpec skSpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
        int maxKeyLen = Cipher.getMaxAllowedKeyLength("AES");

        Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, skSpec, iv);
        byte[] original = cipher.doFinal(encrypted.getBytes());

        return new String(original);
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
}
