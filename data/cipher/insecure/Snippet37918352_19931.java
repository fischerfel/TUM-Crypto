    private Key getAesKey() throws Exception {
    return new SecretKeySpec(Arrays.copyOf(key.getBytes("UTF-8"), 16), "AES");
}

private Cipher getMutual() throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    return cipher;// cipher.doFinal(pass.getBytes());
}

public byte[] getEncryptedPass(String pass) throws Exception {
    Cipher cipher = getMutual();
    cipher.init(Cipher.ENCRYPT_MODE, getAesKey());
    byte[] encrypted = cipher.doFinal(pass.getBytes("UTF-8"));
    return encrypted;

}

public String getDecryptedPass(byte[] encrypted) throws Exception {
    Cipher cipher = getMutual();
    cipher.init(Cipher.DECRYPT_MODE, getAesKey());
    String realPass = new String(cipher.doFinal(encrypted));
    return realPass;
}
