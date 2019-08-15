/**
     * Method To Decrypt An Ecrypted String
     */
    public String decrypt(String encryptedString, String myEncryptionKey) {
        String decryptedText = null;
        try {
            byte[] keyAsBytes = myEncryptionKey.getBytes("UTF8");
            KeySpec myKeySpec = new DESedeKeySpec(keyAsBytes);
            SecretKeyFactory mySecretKeyFactory = 
                    SecretKeyFactory.getInstance("DESede");
            Cipher cipher = Cipher.getInstance("DESede/ECB/NoPadding");
            SecretKey key = mySecretKeyFactory.generateSecret(myKeySpec);

            cipher.init(Cipher.DECRYPT_MODE, key);
//            BASE64Decoder base64decoder = new BASE64Decoder();
//            byte[] encryptedText = base64decoder.decodeBuffer(encryptedString);

            byte[] encryptedText =  org.apache.commons.codec.binary.Base64.decodeBase64(encryptedString);
            byte[] plainText = cipher.doFinal(encryptedText);
            decryptedText= bytes2String(plainText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedText;
    }
