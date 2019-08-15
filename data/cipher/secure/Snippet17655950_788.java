private byte[] encryptRSA(byte [] data) throws NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        //instance of singleton PublicKey
        AppPublicKey currKey = AppPublicKey.getInstance();
        Log.d("ENCRYPT.MOD: ", currKey.getModBytes().toString());

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(new BigInteger(1,currKey.getModBytes()), new BigInteger(1,currKey.getExpBytes()));
        KeyFactory keyFactory =  KeyFactory.getInstance("RSA");

        PublicKey pubKey = keyFactory.generatePublic(keySpec);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, pubKey);
        byte[] cipherData = cipher.doFinal(data);
        Log.d("RSAENCRYPTION: ",Base64.encodeToString(cipherData, 1));
        return cipherData;

 }
