public class AES {
    public static String s = "long string";

    public byte [] encryptAES(byte[] data, byte[] keyPass) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, UnsupportedEncodingException {

        Cipher ciph = Cipher.getInstance("AES");

        SecretKeySpec AESkeySpec = new SecretKeySpec(keyPass, "AES");
        ciph.init(ENCRYPT_MODE,AESkeySpec);

        ciph.update(s.getBytes());
        byte[] encryptedData = ciph.doFinal();

        return encryptedData;
    }
}
