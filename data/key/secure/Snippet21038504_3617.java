public static String encryptToBase64(String data, String key){
    try {
        byte[] valueByte = encrypt(data.getBytes("utf-8"), key.getBytes("utf-8");
        return new String(Base64.encode(valueByte));
    } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("encrypt fail!", e);
    }

}

 public static byte[] encrypt(byte[] data, byte[] key) {

    if(key.length!=16){
        throw new RuntimeException("Invalid AES key length (must be 16 bytes)");
    }
    try {
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES"); 
        byte[] enCodeFormat = secretKey.getEncoded();
        SecretKeySpec seckey = new SecretKeySpec(enCodeFormat,"AES");
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, seckey);// 初始化
        byte[] result = cipher.doFinal(data);
        return result; // 加密
    } catch (Exception e){
        throw new RuntimeException("encrypt fail!", e);
    }
}
