public static byte[] encrypt(String text) {

        byte[] encodedPublicKey= Base64.decode("LS0tLS1CRUdJTiBSU0EgUFVCTElDIEtFWS0tLS0tTUlHSkFvR0JBS2tzNjJJdG5zMnVVL2RWWko0a0NrTWluSGd5ZWgvcmRNRDUzYTRadTJhNzZPSUp2ZFNaOHE0Y1lUV3ZQajBnaWVmVnRNYzd0VjRjNkFBdzA0anlJZm1DVHZjUVVsSEkrc3NwSHhYRGxRVGFnTm94Q3VBMjliNUw5TUtPNk9rMEx3RjlyR2dUeXdDMWhlTkV1bFp6OUlTbjlGUURhekpUK0JkOWNuTk9ySlJkQWdNQkFBRT0tLS0tLUVORCBSU0EgUFVCTElDIEtFWS0tLS0t", Base64.DEFAULT);



   PublicKey publicKey=null;
    KeyFactory keyFactory = null;
    try {
        keyFactory = KeyFactory.getInstance("RSA");
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        publicKey = keyFactory.generatePublic(publicKeySpec);

    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }catch (InvalidKeySpecException e) {
        e.printStackTrace();
    }


    byte[] cipherText = null;
    try {
        // get an RSA cipher object and print the provider
        final Cipher cipher = Cipher.getInstance("RSA");
        // encrypt the plain text using the public key
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        cipherText = cipher.doFinal(text.getBytes());
    } catch (Exception e) {
        e.printStackTrace();
    }
    return cipherText;
}
