public class SSLClient implements Runnable
{
    private SSLSocketFactory sslSocketFactory;
    private SSLSocket sslSocket;
    private KeyStore keystore;
    private KeyManager[] keyManagers;
    private TrustManager[] trustManagers;
    private SSLContext sslContext;
    private String hostname = "localhost";
    private int port = 8081;
    private static final Character EOL = '\n';
    private static final Character EOF = '\u0017';

    public SSLClient() throws UnknownHostException, IOException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException, CertificateException, NoSuchProviderException, UnrecoverableKeyException
    {
        char[] passphrase = "changeit".toCharArray();        
        this.keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        this.keystore.load(new FileInputStream("keystore.ks"), passphrase);

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
        keyManagerFactory.init(this.keystore, passphrase);

        this.sslContext = SSLContext.getInstance("TLS");

        this.keyManagers = keyManagerFactory.getKeyManagers();

        this.trustManagers = new TrustManager[]{
            new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }
                public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
                {

                }
                public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
                {

                }
            }
        };

        this.sslContext.init(this.keyManagers, this.trustManagers, new SecureRandom());

        this.sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
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
            System.out.println("Start client");

            this.sslSocket = (SSLSocket) this.sslSocketFactory.createSocket(this.hostname, this.port);

            if(this.sslSocket.isConnected())
            {
                inputStream = (InputStream) this.sslSocket.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                input = new BufferedReader(inputStreamReader);

                outputStream = this.sslSocket.getOutputStream();                
                output = new PrintWriter(outputStream);

                System.out.println("  Client -> sending...");

                String clientSend = "Hello server! I am the client!" + EOL + EOF;

                output.println(clientSend);
----->          output.flush(); // exception occurs here!

                System.out.println("    Client sent: " + clientSend);

                StringBuffer receiver = new StringBuffer();
                Character clientReceived;
                while ((clientReceived = (char)input.read()) != EOF)
                {
                    receiver.append(clientReceived);
                }

                System.out.println("    Client received: " + receiver.toString());
            }
            else
            {
                System.err.println("Connection to server lost!");
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
            System.out.println("End client");
        }
    }
}
