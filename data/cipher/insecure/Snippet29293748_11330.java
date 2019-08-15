public class EncDec
{
    private static final String ALGO = "AES";
    private static final byte[] keyValue = "1234567891234567".getBytes();
    public static void main(String[] args) throws Exception 
    {
        String testData = "ABC";
        String enc = encrypt(testData);
        System.out.println("Encrypted data: "+enc);
        String dec = decrypt(enc);
        System.out.println("Decrypted data: "+enc);
    }
    public static String encrypt(String Data) throws Exception 
    {

        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = new BASE64Encoder().encode(encVal);
        return encryptedValue;
    }
    public static String decrypt(String encryptedData) throws Exception 
    {
        try{
            Key key = generateKey();
            Cipher c = Cipher.getInstance(ALGO);
            c.init(Cipher.DECRYPT_MODE, key);
            byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
            byte[] decValue = c.doFinal(decordedValue);
            String decryptedValue = new String(decValue);
            return decryptedValue;
        }catch(Exception e)
        {
            System.out.println("Something wrong..");
            return "";
        }
    }
    private static Key generateKey() throws Exception
    {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
}
