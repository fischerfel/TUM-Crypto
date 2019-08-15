 KeyGenerator keyGenerator = KeyGenerator.getInstance(
         KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
 keyGenerator.initialize(
         new KeyGenParameterSpec.Builder("key2",
                 KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                 .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                 .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                 .build());
 SecretKey key = keyGenerator.generateKey();

 Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
 cipher.init(Cipher.ENCRYPT_MODE, key);
 ...

 // The key can also be obtained from the Android Keystore any time as follows:
 KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
 keyStore.load(null);
 key = (SecretKey) keyStore.getKey("key2", null);
