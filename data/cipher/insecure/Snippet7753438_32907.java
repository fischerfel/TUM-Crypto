public class CipherAES implements Cipher {

    private static final Logger logger = Logger.getLogger(CipherAES.class);

    private Key key;

    public CipherAES() {
        this.key = generateKey();
    }

    private Key generateKey() {
        try {
            KeyGenerator generator;
            generator = KeyGenerator.getInstance("AES");
            generator.init(new SecureRandom());
            return generator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void decrypt(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(javax.crypto.Cipher.DECRYPT_MODE, key);
            byte[] raw = IOUtil.toByteArray(inputStream);
            byte[] base64Decoded = Base64.decodeBase64(raw);
            byte[] decryptedData = cipher.doFinal(base64Decoded);
            outputStream.write(decryptedData);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

    @Override
    public void encrypt(InputStream inputStream, OutputStream outputStream) throws IOException {
        try {
            javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES");
            cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, key);
            byte[] raw = IOUtil.toByteArray(inputStream);
            byte[] encryptedData = cipher.doFinal(raw);
            byte[] base64Encoded = Base64.encodeBase64(encryptedData);
            outputStream.write(base64Encoded);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } finally {
            inputStream.close();
            outputStream.close();
        }
    }

}
