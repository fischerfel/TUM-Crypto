public class TestServer {

public static void main(String[] args) throws Exception{
    //set variables for port number, RSA key size
    final int port = 3344;
    final int RSAKeySize = 1024;
    final String newline = "\n";
    //set public key, sockets, server text, plain text
    PublicKey pubKey = null;
    PrivateKey priKey = null;
    ServerSocket server = null;
    Socket client = null;
    String serverText = "Hello Client! This is an authentication message from server";
    byte[] plainText = serverText.getBytes("UTF8");
    //initialize RSA
    try {
        System.out.println("Start generating RSA key");
        KeyPairGenerator RSAKeyGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        RSAKeyGen.initialize(RSAKeySize,random);
        KeyPair pair = RSAKeyGen.generateKeyPair();
        System.out.println("Finish generating RSA key");
        pubKey = pair.getPublic();
        priKey = pair.getPrivate();
    }catch (GeneralSecurityException e){
        System.out.println(e.getLocalizedMessage() + newline);
        System.out.println("Error initialising encryption. Exiting.\n");
        System.exit(0);
    }
    //initialize cryptography, set cipherText
    Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    System.out.println("RSA cipher object and provider"+cipher.getProvider().getInfo());
    System.out.println("Start Encryption for plainText");
    cipher.init(Cipher.ENCRYPT_MODE, pubKey);
    byte[] cipherText = cipher.doFinal(plainText);
    System.out.println("Finish Encryption to cipherText: ");
    BASE64Encoder base64 = new BASE64Encoder();
    String encryptedValue = base64.encode(cipherText);
    //String encryptedValue = new sun.misc.BASE64Encoder().encode(cipherText);
    System.out.println(new String(cipherText,"UTF8"));
    System.out.println("Base64");
    System.out.println(encryptedValue);
    //initialize socket connection
    try{
        server = new ServerSocket(port);
        client = server.accept();
    }catch(IOException e){
        System.out.println("Error initialising I/O.\n");
        System.exit(0);
    }
    //send server private key
    try{
        System.out.println("Send private key out");
        System.out.println(DatatypeConverter.printHexBinary(priKey.getEncoded()));
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(priKey.getEncoded().length);
        client.getOutputStream().write(bb.array());
        client.getOutputStream().write(pubKey.getEncoded());
        client.getOutputStream().flush();
    }catch (IOException e){
        System.out.println("I/O Error");
        System.exit(0);
    }
    //send cipherText
    ObjectOutputStream obOut = new ObjectOutputStream(client.getOutputStream());
    obOut.writeObject(encryptedValue);
    obOut.flush();
    client.close();
}
