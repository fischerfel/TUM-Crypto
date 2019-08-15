public static String encrypt(String key, String value) {
    try {
        SecretKey secretKey = new SecretKeySpec(Base64.decode(key.getBytes(), Base64.NO_WRAP), "AES");
        AlgorithmParameterSpec iv = new IvParameterSpec(Base64.decode(key.getBytes(), Base64.NO_WRAP));
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

        return new String(Base64.encode(cipher.doFinal(value.getBytes("UTF-8")), Base64.NO_WRAP));

    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
