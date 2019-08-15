public static void main(String[] args) throws Exception {
    SecureRandom secRandom = SecureRandom.getInstance("SHA1PRNG");
    KeyGenerator kg = KeyGenerator.getInstance("AES");
    kg.init(128, secRandom);
    Key secretKey = kg.generateKey();
    Cipher AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    AESCipher.init(Cipher.ENCRYPT_MODE, secretKey, secRandom);
    IvParameterSpec iv = new IvParameterSpec(AESCipher.getIV());
    AESCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    AESCipher.init(Cipher.DECRYPT_MODE, secretKey,iv, secRandom);
}
