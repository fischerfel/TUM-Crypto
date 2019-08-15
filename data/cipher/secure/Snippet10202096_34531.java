public class Encryption

{

static byte[] encrypted;    
public Encryption() throws NoSuchAlgorithmException, NoSuchProviderException
{
    KeyPair keypair;
    KeyPairGenerator keygenerator = KeyPairGenerator.getInstance("RSA");
    SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
    keygenerator.initialize(1024, random);
    keypair = keygenerator.generateKeyPair();
}

public String ENCRYPT(String Algorithm, String Data ) throws Exception
{
    String alg = Algorithm;
    String data=Data;
     if(alg.equals("RSA"))
    {   
        stack enc=new stack();
        //Don't know how to call constructor here
        PublicKey publicKey = keypair.getPublic();
        Cipher cipher;
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
         encrypted = cipher.doFinal(data.getBytes());
        System.out.println("Encrypted String -> " + asHex(encrypted));
    }

     return asHex(encrypted);
}
public String DECRYPT(String Algorithm, String Data ) throws Exception
{   
String alg = Algorithm;
String Decrypted="";
if(alg.equals("RSA"))
{   
    //have to call constructor here to get keypair value
    PrivateKey privateKey = keypair.getPrivate();
    Cipher cipher;  
    cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] dec = cipher.doFinal(encrypted);
    Decrypted=new String(dec);
    System.out.println("Decrypted String[RSA] -> " + Decrypted);

}

return Decrypted.toString();
}
public static String asHex (byte buf[])
{
    StringBuffer strbuf = new StringBuffer(buf.length * 2);
    int i;
    for (i = 0; i < buf.length; i++) 
    {
    if (((int) buf[i] & 0xff) < 0x10)
    strbuf.append("0");
    strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
    }

    return strbuf.toString();
}

}
