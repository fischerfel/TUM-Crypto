public static String encryptDesECB(String data) {
    try {
        DESKeySpec keySpec = newDESKeySpec("431654625bd37673e3b00359676154074a04666a".getBytes("UTF8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
        SecretKey key = keyFactory.generateSecret(keySpec);

        // ENCODE plainTextPassword String
        byte[] cleartext = data.getBytes("UTF8");

        Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        Logger.log(Log.INFO, new String(cipher.doFinal(cleartext)));

        String encrypedPwd = Base64.encodeToString(cipher.doFinal(cleartext), Base64.DEFAULT);

        Logger.log(Log.INFO, encrypedPwd);

        return encrypedPwd;

    } catch (Exception e) {
        Logger.log(e);
        return null;
    }
}
