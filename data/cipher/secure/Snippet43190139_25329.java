private static String cipherString(String string) {


    PBEParameterSpec pbeParameterSpec = new PBEParameterSpec(SALT, 100);
    Cipher cipher;
    try {
        cipher = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
        cipher.init(Cipher.ENCRYPT_MODE, PRIVATE_KEY, pbeParameterSpec);
        byte[] input = string.getBytes();
        byte[] encryptedp = cipher.doFinal(input);

        return encryptedp.toString();
    } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException | IllegalBlockSizeException | BadPaddingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}
