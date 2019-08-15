public static String decryptData(String data) {
    String ret = null;
    try {
        PrivateKey key = KeyFactory.getInstance("RSA").generatePrivate(
                new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT)));

        Cipher cph = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cph.init(Cipher.DECRYPT_MODE, key);
        ret = Base64.encodeToString(cph.doFinal(data.getBytes()),
                Base64.DEFAULT);
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeySpecException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }
    return ret;
}
