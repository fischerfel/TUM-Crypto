public static List<Byte> decrypt(List<Byte> bytes, byte[] key) throws Exception {
    byte[] bytesArray = BytesConverter.toByteArray(bytes);

    SecretKey secretKey = new SecretKeySpec(key, AES);

    Cipher cipher = Cipher.getInstance(AES);
    cipher.init(Cipher.DECRYPT_MODE, secretKey);

    return BytesConverter.toByteList(cipher.update(bytesArray));
}
