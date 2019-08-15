public static String aes_encrypt(String password, String strKey) {
    try {
        byte[] keyBytes = Arrays.copyOf(strKey.getBytes("ASCII"), 16);

        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] cleartext = password.getBytes("UTF-8");
        byte[] ciphertextBytes = cipher.doFinal(cleartext);

        return new String(Hex.encodeHex(ciphertextBytes));

    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (NoSuchPaddingException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (IllegalBlockSizeException e) {
        e.printStackTrace();
    } catch (BadPaddingException e) {
        e.printStackTrace();
    } return null;
}
