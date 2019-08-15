public class testCipher 
{
    public static final String PROVIDER = "SunJCE";
    private static final String ALGORITHM = "AES";
    private static final String aesKey = "some long key";
    static Cipher ecipher;
    static Cipher dcipher;

    public static void main(String[] args)  
    {

    try {

    byte[] buf1 = aesKey.getBytes("UTF-8");
    MessageDigest sha = MessageDigest.getInstance("SHA-256");
    buf1 = sha.digest(buf1);
    buf1 = Arrays.copyOf(buf1, 16);
    SecretKeySpec keySpec = null;
    keySpec = new SecretKeySpec(buf1, "AES");

    ecipher = Cipher.getInstance(ALGORITHM, PROVIDER);
    dcipher = Cipher.getInstance(ALGORITHM, PROVIDER);

    ecipher.init(1, keySpec);
    dcipher.init(2, keySpec, ecipher.getParameters());

    if (args[0].equals("encrypt"))
    System.out.println(encrypt(args[1]));
    else if (args[0].equals("decrypt"))
    System.out.println(decrypt(args[1]));
    else {
    System.out.println("USAGE: encrypt/decrypt '<string>'");
    System.exit(15);
   }

} catch (Exception e) {
    System.exit(5);
} 

}

public static String encrypt(String str) 
{
    try {

        byte[] utf8 = str.getBytes("UTF8");
        byte[] enc = ecipher.doFinal(utf8);
        return new sun.misc.BASE64Encoder().encode(enc);

    } catch (Exception e) {
        System.exit(7);
    }

    return null;
}

public static String decrypt(String str) 
{
    try {
        // Decode base64 to get bytes
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

        // Decrypt
        byte[] utf8 = dcipher.doFinal(dec);

        // Decode using utf-8
        return new String(utf8, "UTF8");
    } catch (Exception e) {
        System.exit(7);
    } 
    return null;
}
}
