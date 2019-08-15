private static final String AES_TOKEN = "my_very_secret_token";

// encrypted is base64 string
public String decrypt(String encrypted) throws Exception {
    byte[] decrypted = Base64.getDecoder().decode(encrypted);
    return new String(aesDecrypt(decrypted), "UTF-8");
}

private byte[] aesDecrypt(byte[] message) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    byte[] token = MessageDigest.getInstance("SHA-256").digest(AES_TOKEN.getBytes());
    SecretKeySpec secretKey = new SecretKeySpec(token, "AES");

    IvParameterSpec iv = new IvParameterSpec(Arrays.copyOf(message, 16));

    cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
    return cipher.doFinal(message);
}
