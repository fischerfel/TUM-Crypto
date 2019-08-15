public String encrypt(String password, String key, String VecI) throws GeneralSecurityException, UnsupportedEncodingException{
    byte[] sessionKey = key.getBytes(); 
    byte[] iv = VecI.getBytes() ; 
    byte[] plaintext = password.getBytes();
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(sessionKey, "DES"), new IvParameterSpec(iv));
    byte[] ciphertext = cipher.doFinal(plaintext);
    String resp = ciphertext.toString();
    return resp;
}
