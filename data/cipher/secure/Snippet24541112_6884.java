protected byte[] decryptData(String alias, byte[] data) throws InvalidKeyException, NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, BadPaddingException, IllegalBlockSizeException, UnsupportedOperationException {
        Log.i(TAG, "decryptData() Decrypt data " + HexStringConverter.byteArrayToHexString(data));
        byte[] decryptedData = null;
        PrivateKey privateKey = getPrivateKey(alias);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedData = cipher.doFinal(data);
        Log.i(TAG, "decryptData() Decrypted data: " + HexStringConverter.byteArrayToHexString(decryptedData));
        return decryptedData;
    }
