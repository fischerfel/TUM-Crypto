private String decryptJava(String encryptedText, String saltValue, String passPhrase, String initVector) {
    String result = "";
    byte[] initVectorBytes = initVector.getBytes(US_ASCII);
    byte[] saltValueBytes = saltValue.getBytes(US_ASCII);
    byte[] encryptedTexttBytes = Base64.decode(encryptedText, Base64.DEFAULT);
    Cipher cipher;
    try {
        final PasswordDeriveBytes password = new PasswordDeriveBytes(passPhrase, saltValueBytes);
        final byte[] keyBytes = password.getBytes(256 / Byte.SIZE);
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        try {
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(initVectorBytes));
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        final byte[] ct = cipher.doFinal(encryptedTexttBytes);
        //result = Base64.encodeToString(ct, Base64.DEFAULT); - **deleted this line**
        try {
            result = new String(ct, "US-ASCII");//** added this line**
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

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
