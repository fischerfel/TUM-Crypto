public static JSONObject encrypt(final String text) {
    JSONObject mJsonObject = null;
    try {

        // 1. generate secret key using AES
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(AES);
        keyGenerator.init(KEYSIZE); // AES is currently available in three key sizes: 128, 192 and 256 bits.The design and strength of all key lengths of the AES algorithm are sufficient to protect classified information up to the SECRET level
        final SecretKey secretKey = keyGenerator.generateKey();

        // 3. encrypt string using secret key
        final byte[] raw = secretKey.getEncoded();
        final SecretKeySpec skeySpec = new SecretKeySpec(raw, AES);
        final Cipher cipher = Cipher.getInstance(AES_CBC_PKCS5_PADDING);
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(new byte[16]));
        final String cipherTextString = Base64.encodeToString(cipher.doFinal(text.getBytes(Charset.forName("UTF-8"))), Base64.DEFAULT);

        // 4. get public key
        final X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(Base64.decode(AppConstants.PUBLIC_KEY, Base64.DEFAULT));
        final KeyFactory keyFactory = KeyFactory.getInstance(RSA);
        final PublicKey publicKey = keyFactory.generatePublic(publicSpec);

        // 6. encrypt secret key using public key
        final Cipher cipher2 = Cipher.getInstance(RSA);
        cipher2.init(Cipher.ENCRYPT_MODE, publicKey);
        final String encryptedSecretKey = Base64.encodeToString(cipher2.doFinal(secretKey.getEncoded()), Base64.DEFAULT);

        mJsonObject = new JSONObject();
        try {
            mJsonObject.put(ENCRYPTED_SECRET_KEY, encryptedSecretKey);
            mJsonObject.put(CIPHER_TEXT_STRING, cipherTextString);
        } catch (final JSONException e) {
            e.printStackTrace();
        }



    } catch (NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException | InvalidKeySpecException | InvalidAlgorithmParameterException e) {
        e.printStackTrace();
    }
    return mJsonObject;
}
