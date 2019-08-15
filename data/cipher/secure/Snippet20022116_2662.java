public String Decrypt(String encryptedKey) {
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} try {
    cipher.init(Cipher.DECRYPT_MODE, privKey);
} catch (InvalidKeyException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
byte[] cipherData = null;
try {
    cipherData = cipher.doFinal(Base64.decode(encryptedKey, Base64.NO_WRAP));
} catch (IllegalBlockSizeException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
} catch (BadPaddingException e) {
    // TODO Auto-generated catch block
    e.printStackTrace();
}
    String cipherString = Base64.encodeToString(cipherData, Base64.NO_WRAP);
    Log.d("SecCom", cipherString);
    return cipherString;
}
