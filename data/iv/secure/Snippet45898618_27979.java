private String encryptJava(String plainText, String saltValue, String passPhrase, String initVector) {//working!!!
    String result = "";

    byte[] initVectorBytes = initVector.getBytes(US_ASCII);
    byte[] saltValueBytes = saltValue.getBytes(US_ASCII);
    byte[] plainTextBytes = plainText.getBytes(UTF_8);

    Cipher cipher;
    try {
        final com.gmail.example.PasswordDeriveBytes password = new com.gmail.example.PasswordDeriveBytes(passPhrase, saltValueBytes);
        final byte[] keyBytes = password.getBytes(256 / Byte.SIZE);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(initVectorBytes));
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }

        final byte[] ct = cipher.doFinal(plainTextBytes);
        result = Base64.encodeToString(ct, Base64.DEFAULT);//**added this line!** 
        //result = new String(ct, "US-ASCII");**-- deleted this line!** 
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    }
    return result;
}
