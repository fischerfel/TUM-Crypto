import android.util.Base64;

    try {
        plainText = user.getBytes("UTF-8");
        user = EncryptInfo(plainText, publicKey);
    } catch (UnsupportedEncodingException ex) {
        ex.getStackTrace();
    }

private static String EncryptInfo(byte[] data,PublicKey key){
    Cipher cipher;
    byte[] cipherText = null;
    try {
        cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cipherText = cipher.doFinal(data);
    } catch (NoSuchAlgorithmException ex) {
        ex.getStackTrace();
        System.exit(1);
    } catch (NoSuchPaddingException ex) {
        ex.getStackTrace();
        System.exit(1);
    } catch (InvalidKeyException ex) {
        ex.getStackTrace();
        System.exit(1);
    } catch (IllegalBlockSizeException ex) {
        ex.getStackTrace();
        System.exit(1);
    } catch (BadPaddingException ex) {
        ex.getStackTrace();
        System.exit(1);
    }

    return Base64.encodeToString(cipherText, Base64.DEFAULT);
}
