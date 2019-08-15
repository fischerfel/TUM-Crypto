public String DesDecryptPin(String pin, String encryptKey) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        String UNICODE_FORMAT = "UTF8";
        String decryptedPinText = null;

        byte[] hexConvert = hexStringtoByteArray(encryptKey);

        SecretKey desKey = null;
        byte[] tdesKey = new byte[24];
        System.arraycopy(hexConvert, 0, tdesKey, 0,16);
        System.arraycopy(hexConvert, 0, tdesKey, 0,8);

        byte[] encryptKeyBytes = encryptKey.getBytes(UNICODE_FORMAT);

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

       byte[] decryptPin = desCipher.doFinal(pin.getBytes());
        decryptedPinText = new String(decryptPin, "UTF-8");
        return decryptedPinText;
    }
