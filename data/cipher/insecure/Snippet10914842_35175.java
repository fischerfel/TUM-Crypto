public static byte[] hexDecode(String hex) {
    ByteArrayOutputStream bas = new ByteArrayOutputStream();
    for (int i = 0; i < hex.length(); i+=2) {
        int b = Integer.parseInt(hex.substring(i, i + 2), 16);
        bas.write(b);
    }
    return bas.toByteArray();
}

byte[] key = hexDecode("<hex representation of the Ruby key>");
byte[] iv = hexDecode("<hex representation of the Ruby IV>");

DESedeKeySpec desKeySpec = new DESedeKeySpec(key);
SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DESede");
Key secretKey = keyFactory.generateSecret(desKeySpec);
IvParameterSpec ivSpec = new IvParameterSpec(iv);
Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
... /* do the decryption */
