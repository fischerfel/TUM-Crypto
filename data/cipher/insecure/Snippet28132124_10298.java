private static void decryptRSA()
    {
        FileInputStream cipherfile;
        try {
            cipherfile = new FileInputStream(inFile);

        byte[] ciphertext = new byte[cipherfile.available()];

        PrivateKey privatekey = getKeyPair().getPrivate();

        /* Create cipher for decryption. */

        Cipher decrypt_cipher = Cipher.getInstance("AES");
        decrypt_cipher.init(Cipher.DECRYPT_MODE, privatekey);

        /* Reconstruct the plaintext message. */


        byte[] plaintext = decrypt_cipher.doFinal(ciphertext);
        FileOutputStream plainfile = new FileOutputStream(outFile);
        plainfile.write(plaintext);
        plainfile.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

private static KeyPair getKeyPair() throws Exception
    {
        KeyPair keypair = null;
        FileInputStream is = new FileInputStream(keyStore);
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        keystore.load(is, password.toCharArray());
        Key key = keystore.getKey(keyName, password.toCharArray());
        if (key instanceof PrivateKey) {
            Certificate cert = keystore.getCertificate(keyName);
            PublicKey publicKey = cert.getPublicKey();
            keypair = new KeyPair(publicKey, (PrivateKey) key);
        }
        return keypair;
    }
