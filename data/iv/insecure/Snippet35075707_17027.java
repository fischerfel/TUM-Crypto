private static final String AES_KEY = "HG47YZ3CR8";
public static String decrypt(String orignalText) throws ApplicationException {

    try {
        final MessageDigest md = MessageDigest.getInstance("SHA-256");
        final byte[] digestOfPassword = md.digest(AES_KEY.getBytes("utf-8"));
        final SecretKey key = new SecretKeySpec(digestOfPassword, "AES");

        final Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(new byte[16]));
        final byte[] plainTextBytes = Base64.decodeBase64(orignalText);
        final byte[] encodeTextBytes = cipher.doFinal(plainTextBytes);

        return new String(encodeTextBytes);

    } catch (NoSuchAlgorithmException |
            UnsupportedEncodingException |
            IllegalBlockSizeException |
            InvalidKeyException |
            BadPaddingException |
            NoSuchPaddingException | InvalidAlgorithmParameterException e) {
        throw new ApplicationException(ErrorCode.GENERAL_FAIL, e);
    }
}
