static String AESEncryptStringWithPassword(String s, String p) throws...{
    //function to create key from string password
    SecretKey secret = deriveAESKey(p);
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    cipher.init(Cipher.ENCRYPT_MODE, secret);
    AlgorithmParameters params = cipher.getParameters();
    iv = params.getParameterSpec(IvParameterSpec.class).getIV();
    byte[] ciphertext = cipher.doFinal(s.getBytes(Charset.forName("UTF-8")));
    String str = Base64.getEncoder().encodeToString(ciphertext);
    return str;

}
