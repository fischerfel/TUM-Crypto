public class AES256Cipher {
    public byte[] decrypt(byte[] textBytes, Context context)
            throws java.io.UnsupportedEncodingException,
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        byte[] keyBytes = CommonFunctions.md5Package(context).getBytes();
        byte[] ivBytes = new byte[16];
        for (int j = 0; j < keyBytes.length; j+=2) {
            ivBytes[j/2]=keyBytes[31-j];
        }

        AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, newKey, ivSpec);
        return cipher.doFinal(textBytes);
    }
}
