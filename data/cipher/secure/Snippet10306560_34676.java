private void test() {
    String message="22223334490384903432221";
try {
    //Encrypt message
    byte[] encryptedMsg = Base64.encodeBase64(encryptString(message, temp.loadCASPublicKey()));
} catch (Exception e) {
e.printStackTrace();
        return;
}

}

private static byte[] encryptString(String message, Key publicKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);

    byte[] cipherData = cipher.doFinal(message.getBytes("UTF-8"));     
    return cipherData;
}
