public class KeysHandler {

    /***
     * Generate and store in AndroidKeyStore a security KeyPair keys.
     * @param alias - Alias to create the key.
     * @return KeyPair object with: private and public key.
     */
    public static KeyPair generateKeyPair(String alias) {
        KeyPair kp = null;
        if (alias != null) {
            try {

                KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
                kpg.initialize(new KeyGenParameterSpec.Builder(alias,
                        KeyProperties.PURPOSE_SIGN |
                        KeyProperties.PURPOSE_VERIFY |
                        KeyProperties.PURPOSE_ENCRYPT |
                        KeyProperties.PURPOSE_DECRYPT)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_PKCS1)
                        .build());

                kp = kpg.generateKeyPair();

            } catch (NoSuchProviderException | NoSuchAlgorithmException | InvalidAlgorithmParameterException ex) {
                kp = null;
            }
        }
        return kp;
    }

    public static String encryptString(String alias, String textToEncrypt) {
        String cipheredText = null;

        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);

            // Encrypt the text
            if(textToEncrypt != null && textToEncrypt.length() > 0) {

                Cipher input = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
                input.init(Cipher.ENCRYPT_MODE, privateKeyEntry.getCertificate().getPublicKey());

                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                CipherOutputStream cipherOutputStream = new CipherOutputStream(
                        outputStream, input);
                cipherOutputStream.write(textToEncrypt.getBytes("UTF-8"));
                cipherOutputStream.close();

                byte[] vals = outputStream.toByteArray();
                cipheredText = Base64.encodeToString(vals, Base64.DEFAULT);
            }
        } catch (Exception e) {
            cipheredText = null;
        }

        return cipheredText;
    }


    public static String decryptString(String alias, String cipheredText) {

        String clearText = null;
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);

            Cipher output = Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL");
            output.init(Cipher.DECRYPT_MODE, privateKeyEntry.getPrivateKey());

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new ByteArrayInputStream(Base64.decode(cipheredText, Base64.DEFAULT)), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte)nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for(int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }

            clearText = new String(bytes, 0, bytes.length, "UTF-8");

        } catch (Exception e) {
            clearText = null;
        }

        return clearText;
    }
}
