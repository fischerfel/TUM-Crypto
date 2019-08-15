public class Encrypters {
    public static byte[] AESEncrypt(Key key, byte[] data) throws GeneralSecurityException {
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedData=cipher.doFinal(data);
        return encryptedData;
    }
}
