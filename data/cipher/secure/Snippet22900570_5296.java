public static String encrypt(Key publicKey, String inputText){
    byte[]encodedBytes=null;
    String encryptedText="";
    try {
        Cipher cipher=Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encodedBytes=cipher.doFinal(inputText.getBytes());
    } catch (Exception e) {Log.e("Error", "RSA encryption error");  }

    encryptedText=Base64.encodeToString(encodedBytes, Base64.DEFAULT);
    return encryptedText;
}
