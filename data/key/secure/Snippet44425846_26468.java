public static byte[] GCMEncrypt(String hexKey, String hexIV, byte[] aad) throws Exception {
        byte[] aKey = hexStringToByteArray(hexKey);
        byte[] aIV = hexStringToByteArray(hexIV);
        Key key = new SecretKeySpec(aKey, "AES");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key, new GCMParameterSpec(16 * Byte.SIZE, aIV));
        cipher.updateAAD(aad);
        byte[] encrypted = cipher.doFinal(aKey);
        return encrypted;
    }
