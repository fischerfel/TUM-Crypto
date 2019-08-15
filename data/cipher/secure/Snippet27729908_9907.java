public  String decrypt(byte[] text, PrivateKey key) {
    byte[] dectyptedText = null;
    try {
        final Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, key);
        dectyptedText = cipher.doFinal(text); // this is where I get error when trying to use getBytes() to convert encrypted string to byte[]
    } catch (Exception ex) {
        ex.printStackTrace();
    }
    return new String(dectyptedText);
}
