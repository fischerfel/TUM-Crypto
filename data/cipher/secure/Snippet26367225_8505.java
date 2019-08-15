  PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec(ConvertData.hexStringToByteArray(hexPrivateKey));
        KeyFactory kf;
        PrivateKey privateKey;
        Cipher cipher;
        kf = KeyFactory.getInstance("RSA");
        privateKey = kf.generatePrivate(privSpec);
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        byte[] encryptedBytes = ConvertData.hexStringToByteArray(hexEncodedData);
        int keySize = keyMode;
        int blockSize = (keySize / 8);

        int blocksCount = encryptedBytes.length / blockSize;
        int i = 0;
        int n = blocksCount;
        int startIndex;
        int endIndex;
        byte[] byteChunkData;
        byte[] decryptedChunk;
        byte[] decryptedMessage = new byte[0];
        while (i < n) {
            startIndex = i * (blockSize);
            endIndex = startIndex + blockSize;
            byteChunkData = Arrays.copyOfRange(encryptedBytes, startIndex, endIndex);
            decryptedChunk = cipher.doFinal(byteChunkData);
            decryptedMessage = concatenateByteArrays(decryptedMessage, decryptedChunk);
            i++;
        }
        String plainText = new String(decryptedMessage, ENCODING);
        return plainText;
