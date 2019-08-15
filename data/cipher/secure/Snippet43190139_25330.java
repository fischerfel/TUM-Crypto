private static String decipherString(String string) {
    Cipher c;
    try {
        c = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");

        c.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);

        byte[] input = string.getBytes();
        byte[] encryptedp = c.doFinal(input);

        return encryptedp.toString();

    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    return null;
}
