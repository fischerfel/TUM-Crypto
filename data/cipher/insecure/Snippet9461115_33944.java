EncrypterDecrypter(SecretKey key)
{
    try {
        ecipher = Cipher.getInstance("DES");
        dcipher = Cipher.getInstance("DES");
        ecipher.init(Cipher.ENCRYPT_MODE, key);
        dcipher.init(Cipher.DECRYPT_MODE, key);

    } catch (javax.crypto.NoSuchPaddingException e) {
    } catch (java.security.NoSuchAlgorithmException e) {
    } catch (java.security.InvalidKeyException e) {
    }
}   
