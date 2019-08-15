private String encryptString(String value){
    byte[] encodedBytes = null;
    try {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
        cipher.init(Cipher.ENCRYPT_MODE,  pubKey);
        encodedBytes = cipher.doFinal(value.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }

    return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
}
