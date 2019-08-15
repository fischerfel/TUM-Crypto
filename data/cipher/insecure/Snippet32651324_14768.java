public class EncryptionTest{
Cipher c;
static SecretKey  secKey;
static IvParameterSpec iv;

public static void main(String[] args) throws Exception
{
    KeyGenerator keygen=KeyGenerator.getInstance("DES");
    secKey=keygen.generateKey();
    byte[] encoded = secKey.getEncoded();

    String KeyString=bytesToHex(encoded);
    System.out.println(KeyString);

    SecureRandom rnd = new SecureRandom();
    iv = new IvParameterSpec(rnd.generateSeed(8));


    byte[] ivencoded=iv.getIV();
    String ivString=bytesToHex(ivencoded);

    System.out.println(ivString);

    new EncryptionTest().start();

}
// encoded the key to hex so that i can use at Linux machine to decrypt the data.
public static String bytesToHex(byte[] bytes) {
    char[] hexArray = "0123456789ABCDEF".toCharArray();
    char[] hexChars = new char[bytes.length * 2];
    for ( int j = 0; j < bytes.length; j++ ) {
        int v = bytes[j] & 0xFF;
        hexChars[j * 2] = hexArray[v >>> 4];
        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
    }
    return new String(hexChars);
}

public void start() throws Exception
{
    System.out.println("Encryption/Decryption App");

    c = Cipher.getInstance("DES/CBC/PKCS5Padding");


    String input ="Data to be ecrypted";

    byte[] text = input.getBytes();

    System.out.println("--------------------------------------------");

    System.out.println("Text : " + new String(text));

    byte[] textEncrypted = encrypt(text, c);


    String encryptedString=bytesToHex(textEncrypted);

    System.out.println("Text Encrypted : " + encryptedString);

    byte[] textDecrypted = decrypt(textEncrypted, c);

    System.out.println("Text Decrypted : " + new String(textDecrypted));

    System.out.println("--------------------------------------------");
   }

public byte[] encrypt(byte[] b, Cipher c) throws Exception
{
    c.init(Cipher.ENCRYPT_MODE, secKey,iv);
    byte[] encryptedText = null;
    try {
        encryptedText = c.doFinal(b);
    } catch (IllegalBlockSizeException e) {
        System.out.println("ERROR - error occured");
        System.exit(0);
    } 
    return encryptedText;
}

public byte[] decrypt(byte[] b, Cipher c) throws Exception
{
    c.init(Cipher.DECRYPT_MODE, secKey,iv);

    byte[] decryptedText = c.doFinal(b);
    return decryptedText;
} 
}
