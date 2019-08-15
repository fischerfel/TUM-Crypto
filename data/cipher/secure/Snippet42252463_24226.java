public static String toHex(String arg) {
    return String.format("%x", new BigInteger(1, arg.getBytes()));
}
public static String AesEncrypt(String encryptedMessage){
    try {
        IvParameterSpec initialVector = new IvParameterSpec("xxxxxxxxxxxxxxxx".getBytes("UTF-8"));
        SecretKeySpec secretKeySpec = new SecretKeySpec("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx".getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, initialVector);

        byte[] encrypted = cipher.doFinal(encryptedMessage.getBytes());
        StringBuilder encryptedSb = new StringBuilder(encrypted.length);
        for (byte i : encrypted){
            encryptedSb.append(i);
        }
        return toHex(encryptedSb.toString());
    } catch (Exception ex) {
        ex.printStackTrace();
        return null;
    }
}
