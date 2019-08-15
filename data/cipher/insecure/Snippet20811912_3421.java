public static String decrypt(String content, String sKey) {
    try {
        SecretKey secretKey = null;
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(sKey.getBytes());
        kgen.init(128, secureRandom);
        secretKey = kgen.generateKey();

        byte[] data = ByteUtil.parseHexStr2Byte(content);
        byte[] enCodeFormat = secretKey.getEncoded();

        SecretKeySpec key = new SecretKeySpec(enCodeFormat, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] result = cipher.doFinal(data);
        return new String(result, "UTF-8");
    } catch (Exception e) {
        e.printStackTrace();
    }
    return content;
}
