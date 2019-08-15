public static final byte[] IV = { 65, 1, 2, 23, 4, 5, 6, 7, 32, 21, 10, 11, 12, 13, 84, 45 };
public static String newEncrypt(String text, String key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] keyBytes= new byte[16];
    byte[] b= key.getBytes("UTF-8");
    int len = 16; 
    System.arraycopy(b, 0, keyBytes, 0, len);
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(IV);
    System.out.println(ivSpec);
    cipher.init(Cipher.ENCRYPT_MODE,keySpec,ivSpec);
    byte[] results = cipher.doFinal(text.getBytes("UTF-8"));
    String result = DatatypeConverter.printBase64Binary(results);
    return result;
}
