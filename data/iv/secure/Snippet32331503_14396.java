public static byte[] decodeFile(String key, byte[] fileData) throws Exception
{
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //this parameters should not be changed
    byte[] keyBytes = new byte[16];
    byte[] b = key.getBytes("UTF-16");
    System.out.println("RAM"+b);
    int len = b.length;
    if (len > keyBytes.length)
        len = keyBytes.length;
    System.arraycopy(b, 0, keyBytes, 0, len);
    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);
    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
    byte[] decrypted = cipher.doFinal(fileData);
    return decrypted;
}
