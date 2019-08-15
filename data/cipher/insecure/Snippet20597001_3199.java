try {
    m_encrypt = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
    m_encrypt.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keys.getEncryptKey().getBytes(), "Blowfish"));
    m_decrypt = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
    m_decrypt.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keys.getDecryptKey().getBytes(), "Blowfish"));
} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
    e.printStackTrace();
} catch (InvalidKeyException e) {
    e.printStackTrace();
}
