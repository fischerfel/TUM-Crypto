public class SSLServer implements Runnable
{    
    private SSLServerSocketFactory sslServerSocketFactory;
    private SSLServerSocket sslServerSocket;
    private SSLSocket sslSocket;
    private KeyStore keystore;
    private KeyManager[] keyManagers;
    private SSLContext sslContext;
    private SSLSession sslSession;
    private int port = 8081;
    private static final Character EOL = '\n';
    private static final Character EOF = '\u0017';

    public SSLServer() throws IOException, NoSuchAlgorithmException, KeyManagementException, KeyStoreException, CertificateException, UnrecoverableKeyException
    {
        char[] passphrase = "changeit".toCharArray();
        this.keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        this.keystore.load(new FileInputStream("keystore.ks"), passphrase);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(this.keystore, passphrase);

        this.sslContext = SSLContext.getInstance("TLS");

        this.keyManagers = keyManagerFactory.getKeyManagers();

        this.sslContext.init(this.keyManagers, null, null);

        this.sslServerSocketFactory = this.sslContext.getServerSocketFactory();
        this.sslServerSocket = (SSLServerSocket) this.sslServerSocketFactory.createServerSocket(this.port);
        this.sslServerSocket.setSoTimeout(30000);
        this.sslServerSocket.setEnabledProtocols(new String [] { "TLSv1", "TLSv1.1", "TLSv1.2" });
        this.sslServerSocket.setUseClientMode(false);
        this.sslServerSocket.setWantClientAuth(false);
        this.sslServerSocket.setNeedClientAuth(false);
    }

    @Override
    public void run()
    {
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader input = null;
        OutputStream outputStream = null;
        PrintWriter output = null;

        try
        {
            System.out.println("Server started");

            System.out.println("  Waiting for connection from client...");
            this.sslSocket = (SSLSocket) this.sslServerSocket.accept();

            // Connection was accepted
            System.out.println("  Accepted connection from " + this.sslSocket.getInetAddress().getHostAddress() + ", port " + this.sslSocket.getPort());

            // set up a SSL session
            this.sslSession = this.sslSocket.getSession();
            System.out.println("  Cipher suite used for this session: " + this.sslSession.getCipherSuite());

            inputStream = (InputStream) this.sslSocket.getInputStream();
            inputStreamReader = new InputStreamReader(inputStream);
            input = new BufferedReader(inputStreamReader);

            outputStream = this.sslSocket.getOutputStream();
            output = new PrintWriter(outputStream);

            System.out.println("  Server -> receiving...");
            StringBuffer receiver = new StringBuffer();
            Character serverReceived;
            while ((serverReceived = (char)input.read()) != EOF)
            {
                receiver.append(serverReceived);
            }
            System.out.println("    Server received: " + serverReceived);

            System.out.println("  Server -> sending...");

            String serverSendSuccess = "Hello client, how are you?" + EOL + EOF;
            String serverSendFail = "Who are you?" + EOL + EOF;

            if (receiver.toString().contains("Hello server! I am the client!"))
            {
                System.out.println("    Server sent: " + serverSendSuccess);
                output.println(serverSendSuccess);
                output.flush();
            }
            else
            {
                System.out.println("    Server sent: " + serverSendFail);
                output.println(serverSendFail);
                output.flush();
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            try
            {
                inputStream.close();
                outputStream.close();
                this.sslSocket.close();
            }
            catch(Exception ex) {}
            System.out.println("Server ended");
        }
    }
}
