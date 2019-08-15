             private static class FingerprintCallback extends FingerprintManagerCompat.AuthenticationCallback {

        @Override
        public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        Log.e("test", helpString);
            }
        }

        @Override
        public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
                Log.e("test", "success!");
        }

        @Override
        public void onAuthenticationFailed() {
          Log.e("test", "failed!");
        }

        @Override
        public void onAuthenticationError(int errMsgId, CharSequence errString) {
                Log.e("test", errString);
        }
    }

    private void createKey() {
        try {
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder
            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE_PROVIDER);
            keyGenerator.init(new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT|KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                    .build());
            keyGenerator.generateKey();
        } catch (Exception e) {
            Log.e("Failed to create key:" + e.getLocalizedMessage());
        }
    }

    private SecretKey getSecretKey() {
        SecretKey key = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE_PROVIDER);
            keyStore.load(null);

            key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            if (key == null) {
                createKey();
                key = (SecretKey) keyStore.getKey(KEY_NAME, null);
            }
        } catch (KeyStoreException|CertificateException|UnrecoverableKeyException|NoSuchAlgorithmException|IOException e) {
            Log.e("Failed to init secret key:" + e.getLocalizedMessage());
        }
        return key;
    }
    
    private Cipher getCipher() {
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKey());
        } catch (Exception e) {
            Log.e("Failed to init Cipher:" + e.getLocalizedMessage());
        }
        return cipher;
    }

    public void startListening(FingerprintManagerCompat.CryptoObject cryptoObject) {
        mCancellationSignal = new CancellationSignal();
        mFingerprintManager.authenticate(cryptoObject, 0 /* flags */, mCancellationSignal, new FingerprintCallback(), null);
    }

  @Override
  public void onResume() {
    super.onResume();
    startListening(new FingerprintManagerCompat.CryptoObject(getCipher()));
  }
  