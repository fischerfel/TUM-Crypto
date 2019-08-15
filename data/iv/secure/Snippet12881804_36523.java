private static String Decrypt(String encryptedText, String completeEncodedKey,int keySize) {
        //get completeEncodedKey in bytes and then to string
        String decodedcompleteEncodedKey = StringUtils.newStringUtf8(Base64.decodeBase64(completeEncodedKey));
        System.out.println("Decoded completeEncodedKey Key ::  "+decodedcompleteEncodedKey);
        int indexComma = decodedcompleteEncodedKey.indexOf(',');
        System.out.println("COmma Index :: "+indexComma);
        String IV = decodedcompleteEncodedKey.substring(0, indexComma);
        String Key = decodedcompleteEncodedKey.substring(indexComma+1,decodedcompleteEncodedKey.length());
        System.out.println("IV::: "+IV);
        System.out.println("Key::: "+Key);


    byte[] sessionKey = null; 
    byte[] iv = null ; 
    byte[] plaintext = encryptedText.getBytes(); 
    Cipher cipher = null;
    try {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(sessionKey, "AES"), new IvParameterSpec(iv));
        byte[] ciphertext = cipher.doFinal(plaintext);
    } catch (IllegalBlockSizeException e) {
        System.out.println("IllegalBlockSizeException");
        e.printStackTrace();
    } catch (BadPaddingException e) {
        System.out.println("BadPaddingException");
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        System.out.println("InvalidKeyException");
        e.printStackTrace();
    } catch (InvalidAlgorithmParameterException e) {
        System.out.println("InvalidAlgorithmParameterException");
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        System.out.println("NoSuchAlgorithmException");
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        System.out.println("NoSuchPaddingException");
        e.printStackTrace();
    }
    return null;
}
