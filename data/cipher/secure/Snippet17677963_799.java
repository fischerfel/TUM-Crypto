public class rsa {

private PrivateKey rsaPrivate;
private PublicKey rsapublic;
private Cipher cipher=null;
private  final String ALGORITHM = "RSA";
private  final String PROVIDER = "BC";

public rsa() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException
{
this.init();    
}

public void init() throws NoSuchAlgorithmException, NoSuchPaddingException, NoSuchProviderException
{
    Security.addProvider(new BouncyCastleProvider());
    KeyPairGenerator keyGen = KeyPairGenerator.getInstance(ALGORITHM,PROVIDER);  
    keyGen.initialize(1024);  
    KeyPair keyPair = keyGen.generateKeyPair();  
    this.setRsaPrivate(keyPair.getPrivate())  ;  
    this.setRsapublic(keyPair.getPublic());  
}


     ********* Getter**(){} AND **Setter**(){} methods are removed **********


public String encryption(String Message) throws InvalidKeyException,IllegalBlockSizeException,
                                                BadPaddingException, NoSuchAlgorithmException, 
                                                NoSuchProviderException, NoSuchPaddingException, 
                                                UnsupportedEncodingException
{
    cipher=Cipher.getInstance("RSA/ECB/PKCS1Padding",PROVIDER);
    cipher.init(Cipher.ENCRYPT_MODE,this.getRsapublic());  
    byte[] encryptedMsg = cipher.doFinal(Message.getBytes());  
    return  new String(encryptedMsg); 
}


public String decryption(String encryptedMsg) throws InvalidKeyException, IllegalBlockSizeException,
                                                    BadPaddingException, UnsupportedEncodingException,
                                                    NoSuchAlgorithmException, NoSuchProviderException,
                                                    NoSuchPaddingException
{ 

    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding",PROVIDER);
    cipher.init(Cipher.DECRYPT_MODE,this.getRsaPrivate());
    byte[] dectyptedText = cipher.doFinal(encryptedMsg.getBytes()); 
    return new String(dectyptedText);
}

public static void main(String args[]) throws Exception
{
    rsa r=new rsa();
    System.out.println("Test1 encrypt normal: "+Base64.encodeBase64String(r.encryption("123456").getBytes()));
    System.out.println("Test2 decrypt normal: "+r.decryption(r.encryption("123456")));
}
}
