public String encryptAES(String key, String mdp) throws NoSuchPaddingException, NoSuchAlgorithmException {
    byte[] skey = key.getBytes();
    byte[] pwd = mdp.getBytes();
    byte[] encrypted = null;
    SecretKeySpec secretKeySpec = new SecretKeySpec(skey, "AES");
    Cipher cipher = Cipher.getInstance("AES");
    try {
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    }
    try {
        encrypted = cipher.doFinal(pwd);
    } catch (IllegalBlockSizeException | BadPaddingException e) {
        e.printStackTrace();
    }
    return Arrays.toString(Base64.encode(encrypted, Base64.DEFAULT));
}
