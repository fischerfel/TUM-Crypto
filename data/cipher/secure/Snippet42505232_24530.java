    public void RSADecrypt(final byte[] encryptedBytes, String alias) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

        KeyStore keyStore = null;
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
        } catch (Exception e) {
            Log.e("TAG", Log.getStackTraceString(e));
            e.printStackTrace();
        }
        String initialText = startText.getText().toString();
        if (initialText.isEmpty()) {
            Toast.makeText(this, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();

        }
        try {
            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(alias, null);
            RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey sessionKey = keyGen.generateKey();

            Cipher rsaCipher = Cipher.getInstance("RSA");
            rsaCipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] encryptedSessionKey = rsaCipher.doFinal(sessionKey.getEncoded());

// 3. Encrypt the data using the session key (unencrypted)
            Cipher aesCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            aesCipher.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());
            byte[] decryptedBytes = aesCipher.doFinal(encryptedBytes);

            String decrypted = new String(decryptedBytes);
            System.out.println("DDecrypted?????" + decrypted);
            decryptedText.setText(decrypted);
        } catch (Exception e) {
            Toast.makeText(this, "Exception Ddecrpt" + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
            Log.e("TAG", Log.getStackTraceString(e));
            e.printStackTrace();
        }

    }
