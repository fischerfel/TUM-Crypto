    private String toBase64Crypt(String cryptString) {
    try {
        SecretKeySpec key = new SecretKeySpec(pwd.getBytes("UTF8"), "AES");

        byte[] cryptByte = cryptString.getBytes("UTF8"); 

        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        cryptString = Base64.encodeToString(cipher.doFinal(cryptByte),Base64.DEFAULT);

    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    }

    return cryptString;
}
