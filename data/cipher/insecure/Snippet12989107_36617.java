public class PasswordCrypter {
    Cipher ecipher;
    Cipher dcipher;
    SecretKey key;
    DESKeySpec dks;
    SecretKeyFactory skf;
    byte[] psword;

    public PasswordCrypter(String password) {

        try {
            psword = password.getBytes("UTF-16");
            dks = new DESKeySpec(psword);
            skf = SecretKeyFactory.getInstance("DES");
            key = skf.generateSecret(dks);
            ecipher = Cipher.getInstance("DES");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher = Cipher.getInstance("DES");
            dcipher.init(Cipher.DECRYPT_MODE, key);
        } catch (NoSuchAlgorithmException e) {
            throw new CrypterException(e);
        } catch (NoSuchPaddingException e) {
            throw new CrypterException(e);
        } catch (InvalidKeyException e) {
            throw new CrypterException(e);
        } catch (InvalidKeySpecException e) {
            throw new CrypterException(e);
        } catch (UnsupportedEncodingException e) {
            throw new CrypterException(e);
        }

    }

    public byte[] encrypt(byte[] array) {

        try {
            return ecipher.doFinal(array);
        } catch (IllegalBlockSizeException e) {
            throw new CrypterException(e);
        } catch (BadPaddingException e) {
            throw new CrypterException(e);
        }
    }

    public byte[] decrypt(byte[] array) {

        try {
            return dcipher.doFinal(array);
        } catch (IllegalBlockSizeException e) {
            throw new CrypterException(e);
        } catch (BadPaddingException e) {
            throw new CrypterException(e);
        }
    }
}
