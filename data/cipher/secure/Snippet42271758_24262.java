 public static byte[] signHash(byte[] hashToSign) {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
            KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(MY_KEY_ALIAS, null);
            PrivateKey privateKey = keyEntry.getPrivateKey();

            DigestAlgorithmIdentifierFinder hashAlgorithmFinder = new DefaultDigestAlgorithmIdentifierFinder();
            AlgorithmIdentifier hashingAlgorithmIdentifier = hashAlgorithmFinder.find(KeyProperties.DIGEST_SHA256);
            DigestInfo digestInfo = new DigestInfo(hashingAlgorithmIdentifier, hashToSign);
            byte[] hashToEncrypt = digestInfo.getEncoded();
            Cipher cipher = Cipher.getInstance("RSA/ECB/Pkcs1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey); // <= the exception is thrown here
            return cipher.doFinal(hashToEncrypt);
        } catch (Throwable e) {
            Log.e("KeyStoreWrapper", "Error while signing: ", e);
        }
        return "Could not sign the message.".getBytes(StandardCharsets.UTF_16LE);
    }
