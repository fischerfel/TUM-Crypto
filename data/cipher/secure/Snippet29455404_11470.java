public static String encrypt(String plainText, String key){
    try{
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decode(key, Base64.DEFAULT)));
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        return Base64.encodeToString(cipher.doFinal(plainText.getBytes("UTF-8")),Base64.DEFAULT);
    }catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
