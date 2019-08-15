public class Cryptography {

    private static final String TAG = "Cryptography";


    //secretKey64 is genereated from salt and password..!!! (taken directly from server side)

    private static String secretKey64 = "*****1hrHpnONd8ZJ*****c756ikDmZU1v*****8Xjg=";
    private static String secretKeyAlgorithm = "AES";
    private static String cipherAlgorithm = "AES/CBC/PKCS7Padding";
    private static String iv64 = "QmBSbU*****d31DPr*****==";
    private static String charsetName = "UTF-8";
    //  private static int base64Mode = android.util.Base64.DEFAULT;
    private static int base64Mode = android.util.Base64.NO_WRAP;

    public static String encrypt(String plainText) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException
            , IllegalBlockSizeException, BadPaddingException {
        String encrytedText = "";
        if (plainText == null) {
            return null;
        } else {
            SecretKey secretKey = new SecretKeySpec(android.util.Base64.decode(secretKey64, 0), secretKeyAlgorithm);
            IvParameterSpec iv = new IvParameterSpec(android.util.Base64.decode(iv64, 0));
            Cipher cipher = Cipher.getInstance(cipherAlgorithm);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);

            byte[] dataBytes = plainText.getBytes(charsetName);
            encrytedText = Base64.encodeToString(cipher.doFinal(dataBytes), base64Mode);
        }
        // Log.d(TAG, "ENCRYPT >>>> : " + encrytedText);

        return encrytedText;
    }


    public static String decrypt(String encryptedText) throws NoSuchAlgorithmException, NoSuchPaddingException,
            InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException
            , IllegalBlockSizeException, BadPaddingException, NullPointerException {
        String plainText = "";

        SecretKey secretKey = new SecretKeySpec(android.util.Base64.decode(secretKey64, 0), secretKeyAlgorithm);
        IvParameterSpec iv = new IvParameterSpec(android.util.Base64.decode(iv64, 0));
        byte[] dataBytesD = Base64.decode(encryptedText, base64Mode);
        Cipher cipherD = Cipher.getInstance(cipherAlgorithm);
        cipherD.init(2, secretKey, iv);
        //Log.d(TAG, "DECRYPT text:- >>>> : " + encryptedText);
        byte[] dataBytesDecrypted = cipherD.doFinal(dataBytesD);
        try {
            plainText = new String(dataBytesDecrypted);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        //Log.d(TAG, "DECRYPT >>>> : " + plainText);
        return plainText;
    }
}
