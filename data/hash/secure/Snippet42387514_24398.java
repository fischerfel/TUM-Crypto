private SecretKeySpec getKey(String mode, String msgDigest, String encryptionKey, boolean is256) throws Exception {
    byte[] key = encryptionKey.getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance(msgDigest); // This is SHA-256
    key = sha.digest(key);
    if (is256) {  // This is true in our case.
      key = Arrays.copyOf(key, 32);
      this.logger.debug("Secret Key " + DigestUtils.sha256Hex(encryptionKey).substring(0, 32));
    } else {
      key = Arrays.copyOf(key, 16);
      this.logger.debug("Secret Key " + DigestUtils.sha256Hex(encryptionKey).substring(0, 16));
    }
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");
    String modeStr = mode.equals("ECB") ? "AES/ECB/PKCS5Padding" : "AES/CBC/PKCS5Padding";
    cipher = Cipher.getInstance(modeStr);
    return secretKeySpec;
}
