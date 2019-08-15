public static Cipher getCipher(boolean encrypt) throws Exception {
    //https://en.wikipedia.org/wiki/Stream_cipher       
    byte[] key = ("sometestkey").getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("SHA-1");
    key = sha.digest(key);
    key = Arrays.copyOf(key, 16); // use only first 128 bit

    Key k = new SecretKeySpec(key,"AES");
    Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
    if (encrypt) {
        cipher.init(Cipher.ENCRYPT_MODE, k);
    } else {
        cipher.init(Cipher.DECRYPT_MODE, k);
    }
    return cipher;
}
