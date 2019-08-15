public ServerConnection()
{
    private Socket serverSocket = null;
    private SSLSocket sslServerSocket = null;

    private DataOutputStream dos;
    private DataInputStream dis;
    private ObjectOutputStream oos;
    private ObjectInputStream ois;

    private DataOutputStream ssldos;
    private DataInputStream ssldis;
    private ObjectOutputStream ssloos;
    private ObjectInputStream sslois;

    try
    {
        serverSocket = new Socket("localhost", 9000);

        keyMethod();           

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());           
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory)context.getSocketFactory();
        sslServerSocket = (SSLSocket) sslSocketFactory.createSocket("localhost", 9001);  
        sslServerSocket.startHandshake();

        setStreams();

        setSSLStreams();            
    }
    catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | KeyManagementException e)
    {
        System.out.println("ServerConnection 2 " + e.getMessage());
    }

    private void keyMethod()
    {
        String password = "password";
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(ClassLoader.getSystemResourceAsStream("SSL/mykey.jks"), password.toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
        trustManagerFactory.init(trustStore);
    }

    private void setStreams()
    {
        dos = new DataOutputStream(serverSocket.getOutputStream());
        dis = new DataInputStream(serverSocket.getInputStream()); 
        oos = new ObjectOutputStream(serverSocket.getOutputStream());
        ois = new ObjectInputStream(serverSocket.getInputStream()); 
    }

    private void setSSLStreams()
    {
        ssldos = new DataOutputStream(sslServerSocket.getOutputStream()); 
        ssldis = new DataInputStream(sslServerSocket.getInputStream()); 
        ssloos = new ObjectOutputStream(sslServerSocket.getOutputStream()); 
        sslois = new ObjectInputStream(sslServerSocket.getInputStream());  
    }
}
