public class Server2 {
    private static final int NumberOfThreads = 5;
    private static ExecutorService executorService =Executors.newScheduledThreadPool(NumberOfThreads);
    private static final int port = 1111;
    private static ServerSocket serverSocket;
    private static final String privateKeyFile = "privateServer.der";
    private static final String signedCert = "h.crt";

public static void main(String[] args) {
    try{
        serverSocket = new ServerSocket(port);
    }catch (IOException e){
        e.printStackTrace();
    }

    while(true){
        System.out.println("Server is waiting for connection....");
        try {
            final Socket clientSocket = serverSocket.accept();
            System.out.println("connection established....");
            Runnable task = new Runnable() {
                @Override
                public void run() {
                    try {
                        handleClient(clientSocket);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            };
            executorService.execute(task);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

private static void handleClient(Socket clientSocket) throws Exception{
    //for getting the byte input and output
    OutputStream byteOut = clientSocket.getOutputStream();
    InputStream byteIn = clientSocket.getInputStream();
    //for getting the string input and output
    PrintWriter stringOut = new PrintWriter(clientSocket.getOutputStream(),true);
    BufferedReader stringIn = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    //print out client msg
    System.out.println(stringIn.readLine());
    stringOut.println("From Server: I am Server!");
    stringOut.flush();
    System.out.println("Sent to client: I am Server!");

    //receive nonce from client
    String nonceLen = stringIn.readLine();
    byte[] nonce = new byte[Integer.parseInt(nonceLen)];
    readByte(nonce,byteIn);
    System.out.println("Nonce Received!");

    //get private key from privateServer.der
    PrivateKey privateKey = getPrivateKey();

    //use private key to initialize the encryption mode
    Cipher RSAEncrypt = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    RSAEncrypt.init(Cipher.ENCRYPT_MODE,privateKey);

    //encrypt nonce and send it to the client
    byte[] encryptedNonce = RSAEncrypt.doFinal(nonce);
    stringOut.println(Integer.toString(encryptedNonce.length));
    byteOut.write(encryptedNonce,0,encryptedNonce.length);
    System.out.println(encryptedNonce);
    byteOut.flush();
    System.out.println("Sent to client: encrypted nonce");
    }
}
