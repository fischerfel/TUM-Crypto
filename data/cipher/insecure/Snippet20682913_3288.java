public class EncDec {

    public static void main(String[] args) throws IOException
            , InvalidKeyException, NoSuchAlgorithmException
            , NoSuchPaddingException {

        final String MESSAGE = "I'm a secret message";
        final Charset CHARSET = Charset.defaultCharset();

        Cipher cipher = Cipher.getInstance("AES");
        SecretKey key = KeyGenerator.getInstance("AES").generateKey();
        cipher.init(Cipher.ENCRYPT_MODE, key);

        // Encrypt the message
        InputStream plainIn = new ByteArrayInputStream(
                MESSAGE.getBytes(CHARSET));
        ByteArrayOutputStream encryptedOut = new ByteArrayOutputStream();
        copy(plainIn, new CipherOutputStream(encryptedOut, cipher));

        // Decrypt the message
        cipher.init(Cipher.DECRYPT_MODE, key);
        InputStream encryptedIn = new CipherInputStream(
                new ByteArrayInputStream(encryptedOut.toByteArray()), cipher);
        ByteArrayOutputStream plainOut = new ByteArrayOutputStream();
        copy(encryptedIn, plainOut);

        System.out.println(new String(plainOut.toByteArray(), CHARSET));
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] buffer = new byte[4096];
        while ( in.read(buffer) > -1) {
            out.write(buffer);
        }
        out.flush();
    }
}
