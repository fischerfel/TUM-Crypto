public static void lock(String secret, String textToEncrypt) {
    try {
        //Convert the public key string into a key
        byte[] encodedPublicKey = Base64.decode(secret.getBytes("utf-8"),Base64.DEFAULT);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(encodedPublicKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publickey = keyFactory.generatePublic(spec); //Crash Here
        PrivateKey privateKey = keyFactory.generatePrivate(spec);

        //Encrypt Message
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publickey);
        byte[] encryptedBytes = cipher.doFinal(textToEncrypt.getBytes());
        Log.d(TAG,"Encrypted: "+new String(encryptedBytes));
    } catch (Exception e) {
        e.printStackTrace();
    }
}
