public class TestClient {

public static void main(String[] args){ 
    //throws IOException, NoSuchAlgorithmException, NoSuchPaddingException, ClassNotFoundException, 
    //InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    //set variable for port, socket, server public key
    final int port = 3344;
    Socket sock = null;
    Key serverPubKey = null;
    final int RSAKeySize = 1024;
    final String newline = "\n";
    Key priKey = null;
    //setup connection by creating socket
    try{
        sock = new Socket(InetAddress.getLocalHost(),port);
    }catch(UnknownHostException e){
        System.out.println("Unknown host.");
        System.exit(1);
    }catch(IOException e){
        System.out.println("No I/O");
        System.exit(1);
    }
    //get public key from server
    try{
        byte[] lenb = new byte[4];
        sock.getInputStream().read(lenb,0,4);
        ByteBuffer bb = ByteBuffer.wrap(lenb);
        int len = bb.getInt();
        System.out.println(len);
        byte[] servPubKeyBytes = new byte[len];
        sock.getInputStream().read(servPubKeyBytes);
        System.out.println(DatatypeConverter.printHexBinary(servPubKeyBytes));
        X509EncodedKeySpec ks = new X509EncodedKeySpec(servPubKeyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        serverPubKey = kf.generatePublic(ks);
        System.out.println(DatatypeConverter.printHexBinary(serverPubKey.getEncoded()));
        //PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));

    }catch (IOException e) {
        System.out.println("Error obtaining server public key 1.");
        System.exit(0);
    } catch (NoSuchAlgorithmException e) {
        System.out.println("Error obtaining server public key 2.");
        System.exit(0);
    } catch (InvalidKeySpecException e) {
        System.out.println("Error obtaining server public key 3.");
        System.exit(0);
    }
    try {
        System.out.println("Start generating RSA key");
        KeyPairGenerator RSAKeyGen = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        RSAKeyGen.initialize(RSAKeySize, random);
        KeyPair pair = RSAKeyGen.generateKeyPair();
        System.out.println("Finish generating RSA key");
        priKey = pair.getPrivate();
    }catch (GeneralSecurityException e){
        System.out.println(e.getLocalizedMessage() + newline);
        System.out.println("Error initialising encryption. Exiting.\n");
        System.exit(0);
    }
    try{
    //Decrypt message from server
        BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));
        String message = in.readLine();
        //ObjectInputStream obIn = new ObjectInputStream(sock.getInputStream());
        //Object obj = obIn.readObject();
        System.out.println(message);
        byte[] cipherTextFromServer = new BASE64Decoder().decodeBuffer(message);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        System.out.println("Start decryption");
        cipher.init(Cipher.DECRYPT_MODE, priKey);
        byte[] newPlainText = cipher.doFinal(cipherTextFromServer);
        System.out.println("Finish decryption: ");
        System.out.println(new String(newPlainText,"UTF8"));
        sock.close();
    }catch(Exception e){
        e.printStackTrace();
    }   
}
