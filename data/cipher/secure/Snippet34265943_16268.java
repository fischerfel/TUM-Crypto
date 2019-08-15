private String decryptString(String value){
    byte[] decodedBytes = null;
    try {
        Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
        c.init(Cipher.DECRYPT_MODE,  privKey);
        decodedBytes = c.doFinal(Base64.decode(value, Base64.DEFAULT));
    } catch (Exception e) {
        e.printStackTrace();
        Log.e("Error", "Error = " + e);
        return "SECURE_FAILURE";
    }

    return new String(decodedBytes);
}
