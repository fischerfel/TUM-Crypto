
    private void testDES() throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        byte[] keyByte = convertStringToBytes("00 00 00 00 00 00 00 00");
        byte[] data = convertStringToBytes("00 00 00 00 00 00 00 00");
        Key key = new SecretKeySpec(keyByte, "DES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        System.out.println(hexadecimalString(cipher.doFinal(data)));
    }
