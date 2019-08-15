public static String getKeyStoreString(final Context context) {

        KeyStore keyStore;
        String recoveredSecret = "";
        String filesDirectory = context.getFilesDir().getAbsolutePath();
        String encryptedDataFilePath = filesDirectory + File.separator + "my_phrase";
        try {
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);
            SecretKey secretKey = (SecretKey)
                    keyStore.getKey("phrase", null);
            if (secretKey == null) throw new RuntimeException("secretKey is null");

            Cipher outCipher;
            outCipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
            outCipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(
                    new byte[outCipher.getBlockSize()]));

            CipherInputStream cipherInputStream = new CipherInputStream(
                    new FileInputStream(encryptedDataFilePath), outCipher);
            byte[] roundTrippedBytes = new byte[1000]; //TODO: dynamically resize as we get more data
            int index = 0;
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                roundTrippedBytes[index] = (byte) nextByte;
                index++;
            }
            recoveredSecret = new String(roundTrippedBytes, 0, index, "UTF-8");
            Log.e(TAG, "round tripped string = " + recoveredSecret);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.e(TAG, "recovered: " + recoveredSecret);
        return recoveredSecret;
    }
