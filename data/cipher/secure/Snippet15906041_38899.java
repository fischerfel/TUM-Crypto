public class Cryptor {

    private Cipher cipher;
    private String secretKey = "1234567890qwertz";
    private String iv = "1234567890qwertz";

    private SecretKey keySpec;
    private IvParameterSpec ivSpec;
    private Charset CHARSET = Charset.forName("ISO-8859-1"); // ISO-8859-1 vs. UTF-8

    public Cryptor() throws CryptingException {

        keySpec = new SecretKeySpec(secretKey.getBytes(CHARSET), "AES");
        ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));
        try {
            cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            throw new SecurityException(e);
        } catch (NoSuchPaddingException e) {
            throw new SecurityException(e);
        }
    }

    public String decrypt(String input) throws CryptingException {

        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            return new String(cipher.doFinal(input.getBytes(CHARSET)), CHARSET).trim();
        } catch (IllegalBlockSizeException e) {
            throw new SecurityException(e);
        } catch (BadPaddingException e) {
            throw new SecurityException(e);
        } catch (InvalidKeyException e) {
            throw new SecurityException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new SecurityException(e);
        }
    }

    public String encrypt(String input) throws CryptingException {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            return new String(cipher.doFinal(input.getBytes(CHARSET)), CHARSET).trim();
        } catch (InvalidKeyException e) {
            throw new SecurityException(e);
        } catch (InvalidAlgorithmParameterException e) {
            throw new SecurityException(e);
        } catch (IllegalBlockSizeException e) {
            throw new SecurityException(e);
        } catch (BadPaddingException e) {
            throw new SecurityException(e);
        }
    }

    public static void main(String Args[]) {

        try {
            Cryptor c = new Cryptor();
            String original = "MiiiMüäöMeeʞ";
            System.out.println("Original: " + original);
            String encrypted = c.encrypt("MiiiMüäöMeeʞ");
            System.out.println("Encoded: " + encrypted);
            System.out.println("Decoded: " + c.decrypt(encrypted));

        } catch (CryptingException e) {
            e.printStackTrace();
        }
    }

    class CryptingException extends RuntimeException {

        private static final long serialVersionUID = 7123322995084333687L;

        public CryptingException() {
            super();
        }

        public CryptingException(String message) {
            super(message);
        }
    }
}
