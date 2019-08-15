public class TestBlowfish
{
    final String KEY = "92514c2df6e22f079acabedce08f8ac3";
    final String PLAIN_TEXT = "sangasong@song.com";
    byte[] keyBytes = DatatypeConverter.parseHexBinary(KEY); 
}

public static void main(String[] args) throws Exception 
{
    try 
    {
        byte[] encrypted = encrypt(keyBytes, PLAIN_TEXT);
        System.out.println( "Encrypted hex: " + Hex.encodeHexString(encrypted));

    }catch (GeneralSecurityException e) 
    {
        e.printStackTrace();
    }
}

private static byte[] encrypt(byte[] key, String plainText) throws GeneralSecurityException
{
    SecretKey secret_key = new SecretKeySpec(key, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(Cipher.ENCRYPT_MODE, secret_key);

    return cipher.doFinal(plainText.getBytes());
} 
