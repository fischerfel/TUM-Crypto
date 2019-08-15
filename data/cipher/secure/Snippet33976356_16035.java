KeyGenerator keyGen = null;
    try {
        keyGen = KeyGenerator.getInstance("HmacSHA256");
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    }
    SecretKey key = keyGen.generateKey();
    byte[] encoded = key.getEncoded();
    String s=Base64.encodeToString(encoded, Base64.DEFAULT);
    Log.i("Hmac key before encrypt",s);

    try {
        KeyStore keystore = KeyStore.getInstance("AndroidKeyStore");
        keystore.load(null, null);
        KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keystore.getEntry("temp", null);
        RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();

        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherBytes = cipher.doFinal(encoded);

        return Base64.encodeToString(cipherBytes,Base64.DEFAULT);


    } catch (UnrecoverableEntryException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
