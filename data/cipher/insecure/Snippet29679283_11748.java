   public String DesDecryptPin(String pin, String encryptKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        String UNICODE_FORMAT = "UTF8";
        String decryptedPinText = null;



        SecretKey desKey = null;
        byte[] encryptKeyBytes = EncodingUtils.getAsciiBytes(encryptKey);
        byte[] tdesKey = new byte[24];
        System.arraycopy(encryptKeyBytes, 8, tdesKey, 0, 8);
        System.arraycopy(encryptKeyBytes, 0, tdesKey, 8, 16);
        KeySpec desKeySpec = new DESedeKeySpec(tdesKey);
        Cipher desCipher;
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DESede");
        desCipher = Cipher.getInstance("DESede/ECB/NoPadding");
        try {
            desKey = skf.generateSecret(desKeySpec);
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        desCipher.init(Cipher.DECRYPT_MODE, desKey);

        byte[] decryptPin = desCipher.doFinal(EncodingUtils.getAsciiBytes(pin));

        decryptedPinText = new String(decryptPin, "ASCII");
        return decryptedPinText;
    }
