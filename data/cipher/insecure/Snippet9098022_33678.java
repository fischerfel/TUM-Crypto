public class NewEncrypter
{
    private String algorithm = "DESede";
    private Key key = null;
    private Cipher cipher = null;

    public NewEncrypter() throws NoSuchAlgorithmException, NoSuchPaddingException
    {
         key = KeyGenerator.getInstance(algorithm).generateKey();
         cipher = Cipher.getInstance(algorithm);
    }

    public byte[] encrypt(String input) throws Exception
    {
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] inputBytes = input.getBytes("UTF-16");

        return cipher.doFinal(inputBytes);
    }

    public String decrypt(byte[] encryptionBytes) throws Exception
    {
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] recoveredBytes = cipher.doFinal(encryptionBytes);
        String recovered = new String(recoveredBytes, "UTF-16");

        return recovered;
    }
}
