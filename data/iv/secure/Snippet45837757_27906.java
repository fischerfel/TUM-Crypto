private String encryptDES(String sessionKey) throws Exception {
    KeyGenerator keygenerator = KeyGenerator.getInstance("DESede");
    SecretKey myKey = keygenerator.generateKey();
    SecureRandom sr = new SecureRandom(); 
    byte [] iv = new byte[8]; 
    sr.nextBytes(iv); 
    IvParameterSpec IV = new IvParameterSpec(iv);
    Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, myKey, IV);
    String encrypted = Base64.encode(cipher.doFinal(sessionKey.getBytes()));
    return encrypted;
}
