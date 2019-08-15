public static byte[] decryptWithAesCBC(byte[] ciphertext, byte[] key, byte[] iv) {
        try {
            PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
            CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
            aes.init(false, ivAndKey);
            return cipherData(aes, ciphertext);
        }
        catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

private static byte[] cipherData(PaddedBufferedBlockCipher cipher, byte[] data) throws InvalidCipherTextException {
        int minSize = cipher.getOutputSize(data.length);
        byte[] outBuf = new byte[minSize];
        int length1 = cipher.processBytes(data, 0, data.length, outBuf, 0);
        int length2 = cipher.doFinal(outBuf, length1);
        int actualLength = length1 + length2;
        byte[] ciphertext = new byte[actualLength];
        for (int x=0; x < actualLength; x++) {
            ciphertext[x] = outBuf[x];
        }
        return ciphertext;
    }

public static byte[] encryptWithAesCBC(byte[] plaintext, byte[] key, byte[] iv) {
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding", "SC");

            PaddedBufferedBlockCipher aes = new PaddedBufferedBlockCipher(new CBCBlockCipher(new AESEngine()));
            CipherParameters ivAndKey = new ParametersWithIV(new KeyParameter(key), iv);
            aes.init(true, ivAndKey);
            return cipherData(aes, plaintext);
        }
        catch (NoSuchAlgorithmException | NoSuchProviderException | NoSuchPaddingException | InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }
