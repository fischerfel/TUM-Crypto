public String encrypt(String Data, String keySet) throws Exception {
    byte[] keyByte = keySet.getBytes();
    Key key = generateKey(keyByte);

    Cipher c = Cipher.getInstance("AES");

    c.init(Cipher.ENCRYPT_MODE, key); //2
    byte[] encVal = c.doFinal(Data.getBytes()); //1
    byte[] encryptedByteValue = new Base64().encode(encVal); //3
    String encryptedValue = new String(encryptedByteValue); //4
    return encryptedValue;
}

private static Key generateKey(byte[] keyByte) throws Exception {
    Key key = new SecretKeySpec(keyByte, "AES");
    return key;
}
