public String decrypt(String strToBeDecrypted) {
    try {
        strToBeDecrypted = URLDecoder.decode(strToBeDecrypted, "UTF-8");
        DESKeySpec desKeySpec = new DESKeySpec(key);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey skey = keyFactory.generateSecret(desKeySpec);

        IvParameterSpec ivSpec = new IvParameterSpec(iv);

        cipher.init(Cipher.DECRYPT_MODE, skey, ivSpec);

        byte[] keyByteArray = new BASE64Decoder().decodeBuffer(strToBeDecrypted);

        byte[] original = cipher.doFinal(keyByteArray);

        return new String(original, "UTF-8");
    } catch (Exception e) {
        logger.error(ExceptionUtil.getDetailedMessage(e));
    }
    return "";
}
