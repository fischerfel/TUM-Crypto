    private void init(String passphrase) {
        try {
            String algorithm = "PBEWithSHA256And256BitAES-CBC-BC"; 

            encryptCipher = createCipher();
            decryptCipher = createCipher();    

            randomGenerator = new RandomGenerator();

            PBEKeySpec keySpec = new PBEKeySpec(passphrase.toCharArray(), KEY_SALT, ITERATIONS);    

            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(algorithm);
            key = keyFactory.generateSecret(keySpec);    

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("NoSuchAlgorithmException occured while trying to generate the crypto key. This error should never occur, check the application code", e);
        } catch (InvalidKeySpecException e) {
            throw new RuntimeException("InvalidKeySpecException occured while trying to generate the crypto key. This error should never occur, check the application code", e);
        }
    }    

    private BufferedBlockCipher createCipher() {
        return new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESFastEngine()), new PKCS7Padding());
    }    

    public byte[] encrypt(byte[] data) {
        if (data == null)
            throw new NullPointerException("Cannot encrypt null data");    

        byte[] iv = randomGenerator.generateRandom(IV_SIZE);    

        byte[] encrypted;

        synchronized (encryptCipher) {
            encrypted = runCipher(encryptCipher, true, data, iv);
        }    

        return DataUtil.append(iv, encrypted);
    }    

    public byte[] decrypt(byte[] data) {
        if (data == null)
            throw new NullPointerException("Cannot decrypt null data");    

        byte[] iv = DataUtil.extract(data, 0, IV_SIZE);
        byte[] cipherText = DataUtil.extract(data, IV_SIZE, data.length - IV_SIZE);

        byte[] decrypted;    

        synchronized (decryptCipher) {
            decrypted = runCipher(decryptCipher, false, cipherText, iv);
        }

        return decrypted;
    }

    private byte[] runCipher(BufferedBlockCipher cipher, boolean forEncryption, byte[] data, byte[] iv) {
        String operation = forEncryption ? "encrypt" : "decrypt";

        try {
            KeyParameter keyParam = new KeyParameter(key.getEncoded());
            ParametersWithIV cipherParams = new ParametersWithIV(keyParam, iv);

            cipher.init(forEncryption, cipherParams);

            byte[] result = new byte[cipher.getOutputSize(data.length)];
            int len = cipher.processBytes(data, 0, data.length, result, 0);
            len += cipher.doFinal(result, len);

            //Remove padding se estiver decriptografando
            if(!forEncryption)
                result = DataUtil.extract(result, 0, len);

            return result;
        } catch (DataLengthException e) {
            throw new RuntimeException("DataLengthException occured while trying to " + operation + " data with length " + data.length + ". This error should never occur, check the application code", e);
        } catch (IllegalStateException e) {
            throw new RuntimeException("IllegalStateException occured while trying to " + operation + " data with length " + data.length + ". This error should never occur, check the application code", e);
        } catch (InvalidCipherTextException e) {
            throw new IllegalArgumentException("InvalidCipherTextException occured while trying to " + operation + " data with length " + data.length, e);
        }
    }
