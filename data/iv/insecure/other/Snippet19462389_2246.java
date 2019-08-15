public static String Decrypt(String message, String key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    byte[] keyBytes = new byte[16];
    byte[] b = key.getBytes("UTF-8");
    int len = b.length;
    if (len > keyBytes.length) {
        len = keyBytes.length;
    }
    System.arraycopy(b, 0, keyBytes, 0, len);

    SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

    byte[] messageBytes =  DatatypeConverter.parseBase64Binary(message);
    byte[] macBytes = new byte[20];
    byte[] ciphertext = new byte[messageBytes.length - 20];

    System.arraycopy(messageBytes, 0, macBytes, 0, macBytes.length);
    System.arraycopy(messageBytes, 20, ciphertext, 0, ciphertext.length);

    Mac mac = Mac.getInstance("HMACSHA1");
    mac.init(keySpec);

    verifyMac(mac.doFinal(ciphertext), macBytes);

    cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

    byte[] results = cipher.doFinal(ciphertext);
    return new String(results, "UTF-8");
}

private static void verifyMac(byte[] mac1, byte[] mac2) throws Exception {
    MessageDigest sha = MessageDigest.getInstance("SHA1");
    byte[] mac1_hash = sha.digest(mac1);
    sha.reset();
    byte[] mac2_hash = sha.digest(mac2);

    if(!Arrays.equals(mac1_hash, mac2_hash)){
        throw new RuntimeException("Invalid MAC");
    }

}
