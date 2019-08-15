Cipher cipher;
SecretKeySpec skeySpec = new SecretKeySpec(key, "AES");
try {
    cipher = Cipher.getInstance("AES");
} catch (NoSuchAlgorithmException e) {
} catch (NoSuchPaddingException e) {
}try {
      cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
} catch (InvalidKeyException e) {
}
