public static String encryptData(String data) {
    String ret = null;
    try {
        PublicKey key = KeyFactory.getInstance("RSA").generatePublic(
                new X509EncodedKeySpec(Base64.decode(publicKey, Base64.DEFAULT)));

        Cipher cph = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cph.init(Cipher.ENCRYPT_MODE, key);
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
