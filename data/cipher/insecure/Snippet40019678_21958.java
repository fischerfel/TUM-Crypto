    byte[] message = Base64.decodeBase64(encryptedText.getBytes("utf-8"));
    //byte[] message = encryptedText.getBytes("utf-8");

    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] digestOfPassword = md.digest(secretKey.getBytes("utf-8"));
    byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
    SecretKey key = new SecretKeySpec(keyBytes, "DESede");

    //Cipher decipher = Cipher.getInstance("DESede");
    Cipher decipher = Cipher.getInstance("DESede/ECB/PKCS5Padding");
    decipher.init(Cipher.DECRYPT_MODE, key);

    byte[] plainText = decipher.doFinal(message);

    return new String(plainText, "UTF-8");
}
