private void test() {

    try {
        String stringMessage="Sf3O7Lr2+WN5szGyLejL3CjuBRZtQ72+ZBmgVTgWnatQZxUElzaBqFa1p0SVBqe9VWVxCxdEkejMVtDGEr0UJSVSK8EB/fPI6v8JE8dIu0JN0mMs4xlowhITy0tQR+1pcBtDFjzOl33xxQcq5JuPezxRDxFIp+IVkD8FdpqlttEKf2Tvqw9tqsdgiBKb5xDvKrkIDQXdLBh1gbAVZDSJYGHRkcOA8vz2ty/PeooKkfDK6IOn7KBwOBgSRgQr/MLBF3Xk2vRWgVGRh/fRkzu21EWo99Q5moWKxWl3HW/bbgTBQTb097XP3NTID9kSPhCfL0BEfBxonuNse5GBoeRnCw==";
        //Convert String back to Byte[] and decrpt
        byte[] byteMessage = Base64.decodeBase64(stringMessage.getBytes("UTF-8"));
        System.out.println("ENCRYPTED MESSAGE byte Length: "+byteMessage.length);

        String decryptedMsg = decryptString(byteMessage, loadCASPrivateKey());
        System.out.println(decryptedMsg);
    } catch (Exception e) {
        e.printStackTrace();
        return;
    }
}

private static String decryptString(byte[] message, Key privateKey) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);

    byte[] cipherData = cipher.doFinal(message);
    return new String(cipherData, "UTF-8");
}

private PrivateKey loadCASPrivateKey() throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
    InputStream is = getClass().getResourceAsStream( "/keys/app-private.key" );
    if (is == null) {
        System.out.println("NULL");
    }
    byte[] encodedPrivateKey = new byte[(int) 2000];
    is.read(encodedPrivateKey);
    is.close();

    // Generate KeyPair.
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");

    PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
    PrivateKey privateKey = keyFactory.generatePrivate(privateKeySpec);

    return privateKey;

}
