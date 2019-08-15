    private static String encryptDataWithSymmetricKey (SecretKeySpec symmKey, String data) throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {


    // encryption
    byte[] toBeCiphred = data.getBytes("UTF-8");
    String encryptedData = null;

    try {
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, symmKey);
        byte[] encodedBytes = c.doFinal(toBeCiphred);
        System.out.println("BYTE STRING (ASYMM): " + encodedBytes);
        encryptedData = Base64.encodeToString(encodedBytes, Base64.DEFAULT);

    } catch (Exception e) {
        Log.e(TAG, "AES encryption error");
        throw new RuntimeException(e);
    }


    return encryptedData;
}
    encryptedData = encryptDataWithSymmetricKey(symmKey, text);
