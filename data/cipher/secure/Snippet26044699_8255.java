private String encryptString(String value){
    byte[] encodedBytes = null;
    try {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
        cipher.init(Cipher.ENCRYPT_MODE,  privateKeyEntry );
        encodedBytes = cipher.doFinal(value.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }

    return Base64.encodeToString(encodedBytes, Base64.DEFAULT);
}

private String decryptString(String value){
    byte[] decodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
        c.init(Cipher.DECRYPT_MODE,  privateKeyEntry );
        decodedBytes = c.doFinal(Base64.decode(value, Base64.DEFAULT));
    } catch (Exception e) {
        e.printStackTrace();
    }

    return new String(decodedBytes);
}
