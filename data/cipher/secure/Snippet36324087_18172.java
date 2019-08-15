        public static boolean setKeyStoreString(String strToStore, Context context) {

            if (strToStore == null) return false;
            if (strToStore.length() == 0) return false;
            Log.e(TAG, strToStore);
            try {
                KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
                int nBefore = keyStore.size();
                // Create the keys if necessary
                if (!keyStore.containsAlias("phrase")) {
                    KeyGenerator generator = KeyGenerator.getInstance(
                            KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
                    KeyGenParameterSpec spec = new KeyGenParameterSpec.Builder("phrase", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setKeySize(256)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setUserAuthenticationValidityDurationSeconds(-1)
                            .setRandomizedEncryptionRequired(false)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            .setUserAuthenticationRequired(false)
                            .build();
                    generator.init(spec);
                    generator.generateKey(); 
                }
                int nAfter = keyStore.size();
                Log.v(TAG, "Before = " + nBefore + " After = " + nAfter);


                String filesDirectory = context.getFilesDir().getAbsolutePath();
                String encryptedDataFilePath = filesDirectory + File.separator + "my_phrase";
    //            Log.v(TAG, "strPhrase = " + strToStore);
    //            Log.v(TAG, "dataDirectory = " + dataDirectory);
    //            Log.v(TAG, "filesDirectory = " + filesDirectory);
    //            Log.v(TAG, "encryptedDataFilePath = " + encryptedDataFilePath);

                SecretKey secret = (SecretKey) keyStore.getKey("phrase", null);
                Cipher inCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
                inCipher.init(Cipher.ENCRYPT_MODE, secret);
                CipherOutputStream cipherOutputStream = new CipherOutputStream(
                        new FileOutputStream(encryptedDataFilePath), inCipher);
                byte[] bytesToStore = strToStore.getBytes("UTF-8");

                cipherOutputStream.write(bytesToStore);
                try {
                    cipherOutputStream.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                return true;
            } catch (Exception e) {
                Log.e(TAG, Log.getStackTraceString(e));
            }
            return false;
        }
