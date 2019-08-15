 private Cipher myGetCipher() throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException {
    // avoid the default security provider "AndroidOpenSSL" in Android 4.3+ (http://libeasy.alwaysdata.net/network/#provider)
    Cipher c = Cipher.getInstance("ARC4", "BC");
    c.init(Cipher.DECRYPT_MODE, new SecretKeySpec("BrianIsInTheKitchen".getBytes(), "ARC4"));
    return c;
}
