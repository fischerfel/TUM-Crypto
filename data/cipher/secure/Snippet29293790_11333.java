   public class AES2 {

    private static final String TRANSFORMATION = "AES/CFB8/NoPadding";
    private static final String ALGO_MD5       = "MD5";
    private static final String ALGO_AES       = "AES";

    /**
     * See http://www.logikdev.com/2012/12/12/md5-generates-31-bytes-instead-of-32/ form more detail.
     * 
     * @param input
     * @return
     * @throws NoSuchAlgorithmException
     */
    private static String md5(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(ALGO_MD5);
        byte[] messageDigest = md.digest(input.getBytes());
        BigInteger number = new BigInteger(1, messageDigest);
        return String.format("%032x", number);
    }

    public static String decrypt(String encryptedData, String initialVectorString, String secretKey) {
        String decryptedData = null;
        try {
            String md5Key = md5(secretKey);
            SecretKeySpec skeySpec = new SecretKeySpec(md5Key.getBytes(), ALGO_AES);
            IvParameterSpec initialVector = new IvParameterSpec(initialVectorString.getBytes());
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, initialVector);
            byte[] encryptedByteArray = Base64.decode(encryptedData.getBytes(), Base64.DEFAULT);
            byte[] decryptedByteArray = cipher.doFinal(encryptedByteArray);
            decryptedData = new String(decryptedByteArray);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }

        return decryptedData;
    }

}
