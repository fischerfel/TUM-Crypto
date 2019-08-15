public class rsa 
{   
 private KeyPair keypair;       

 public rsa() throws NoSuchAlgorithmException, NoSuchProviderException 
    {
        KeyPairGenerator keygenerator = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
        keygenerator.initialize(1024, random);
        keypair = keygenerator.generateKeyPair();
    }
public String ENCRYPT(String Algorithm, String Data ) throws Exception
{   
    String alg = Algorithm;
    String data=Data;
    byte[] encrypted=new byte[2048];
    if(alg.equals("RSA"))
    {   

        PublicKey publicKey = keypair.getPublic();
        Cipher cipher;
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
         encrypted = cipher.doFinal(data.getBytes());
        System.out.println("Encrypted String[RSA] -> " + encrypted);
    }
    return encrypted.toString();
}
public String DECRYPT(String Algorithm, String Data ) throws Exception
{   
    String alg = Algorithm;
    byte[] Decrypted=Data.getBytes();


    if(alg.equals("RSA"))
    {   

        PrivateKey privateKey = keypair.getPrivate();
        Cipher cipher;  
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] dec = cipher.doFinal(Decrypted);

        System.out.println("Decrypted String[RSA] -> " + dec.toString());

    }
    return Decrypted.toString();
}
public static void main(String[] args) throws Exception
{
    rsa RSA=new rsa();
    RSA.ENCRYPT("RSA", "avinash");
    RSA.DECRYPT("RSA","[B@cb7e2c");
}
