public static String encryptAES(byte[] data, byte[] key, byte[] iv) throws Exception {
    Key k = new SecretKeySpec(key, "AES");
    System.out.println("key set");

    Cipher c = Cipher.getInstance("AES/CBC/PKCS5PADDING");
    System.out.println("cipher created");

    c.init(Cipher.ENCRYPT_MODE, k, new IvParameterSpec(iv));
    System.out.println("cipher initialised");

    byte[] encryptedDataBytes = c.doFinal(data);
    String encryptedData = Base64.getEncoder().encodeToString(encryptedDataBytes);
    return encryptedData;
}
