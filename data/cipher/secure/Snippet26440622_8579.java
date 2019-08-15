private byte[] decrypt(byte[] bytes) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/None/NoPadding");
    cipher.init(Cipher.DECRYPT_MODE, PRIVATE_KEY);
    return cipher.doFinal(bytes);
}

private byte[] encrypt(byte[] bytes) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/None/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, SERVER_KEY);
    return cipher.doFinal(bytes);
}

//Made this method to test my own keys, and whether or not encryption works
private byte[] encryptMyOwnKey(byte[] bytes) throws Exception {
    Cipher cipher = Cipher.getInstance("RSA/None/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, PUBLIC_KEY);
    return cipher.doFinal(bytes);
}
