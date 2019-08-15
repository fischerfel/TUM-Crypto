//create cipher using server's public key
Cipher cipher = null;
try {
    cipher = Cipher.getInstance(serverKey.getAlgorithm(), "SUN");
} catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
} catch (NoSuchPaddingException e) {
    e.printStackTrace();
} catch (NoSuchProviderException e) {
    e.printStackTrace();
}
try {
    cipher.init(Cipher.ENCRYPT_MODE, serverKey);
} catch (InvalidKeyException e) {
    e.printStackTrace();
}
