public class AES256 {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        final KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // doesn't work for 192, too

        final byte[] encoded = keyGen.generateKey().getEncoded();

        final SecretKeySpec keySpec = new SecretKeySpec(encoded, "AES");
        final Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding", "BC");
        // Please ignore static IV for this example
        final IvParameterSpec iv = new IvParameterSpec(new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15});

        c.init(Cipher.ENCRYPT_MODE, keySpec, iv); // throws java.security.InvalidKeyException: Illegal key size
    }
}
