// Server code
class CAServer
{
    private SSLContext ctx;
    private KeyManagerFactory kmf;
    private TrustManagerFactory tmf;
    private SSLServerSocket serverSock;

    public void init() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException, UnrecoverableKeyException, KeyManagementException
    {
        ctx=SSLContext.getInstance("TLS");
        kmf=KeyManagerFactory.getInstance("SunX509");
        tmf=TrustManagerFactory.getInstance("SunX509");
        char[] pwd="111".toCharArray();

        KeyStore ks=KeyStore.getInstance("JKS");
        KeyStore ts=KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("C:/Users/Jim/ca.keystore"), pwd);        
        ts.load(new FileInputStream("C:/Users/Jim/ca.keystore"), pwd); // unused    

        kmf.init(ks,pwd);
        tmf.init(ts);

        TrustManager[] trustClientCerts = new TrustManager[] { new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs,String authType) {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs,String authType) {
            }
        } 
        };


        ctx.init(kmf.getKeyManagers(),trustClientCerts, null);

        //init server
        serverSock=(SSLServerSocket)ctx.getServerSocketFactory().createServerSocket(13000);

        serverSock.setNeedClientAuth(true);
    }

    public void start() throws IOException
    {
        System.out.println("My Secure server start");
        while(true)
        {
            Socket s=serverSock.accept();

            InputStream input=s.getInputStream();   

            byte[] c=new byte[256];
            input.read(c);           **// error(NullPointer) occurs here** 
            System.out.println(new String(c));
        }
    }

}


// Client code
class MyClient
{
    private SSLContext ctx;
    KeyManagerFactory kmf;
    TrustManagerFactory tmf;
    private SSLSocket clientSock;

    public void init() throws NoSuchAlgorithmException, KeyStoreException, CertificateException, FileNotFoundException, IOException, KeyManagementException, UnrecoverableKeyException
    {
        ctx=SSLContext.getInstance("TLS");
        kmf=KeyManagerFactory.getInstance("SunX509");
        tmf=TrustManagerFactory.getInstance("SunX509");

        char[] pwd="111".toCharArray();

        KeyStore ks=KeyStore.getInstance("JKS");
        KeyStore ts=KeyStore.getInstance("JKS");

        ks.load(new FileInputStream("C:/Users/jim/alice.keystore"), pwd);         
        ts.load(new FileInputStream("C:/Users/jim/alice.keystore"), pwd);    //unused  

        TrustManager[] trustServerCerts = new TrustManager[]{new X509TrustManager() 
        {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) 
            {

                for(X509Certificate c :certs){
                   System.out.println(c.getSubjectDN().getName());
                }

             }
            }
        };

        kmf.init(ks, pwd);
        tmf.init(ts);

        ctx.init(kmf.getKeyManagers(), trustServerCerts, null);

        clientSock=(SSLSocket)ctx.getSocketFactory().createSocket("127.0.0.1", 13000);
        clientSock.setUseClientMode(true);

    }

    public void run() throws IOException
    {
        InputStream input = null;  
        OutputStream output = null;  

        output = clientSock.getOutputStream(); 
        BufferedOutputStream bufferedOutput = new BufferedOutputStream(output);
        bufferedOutput.write("Alice: is running".getBytes());  
        bufferedOutput.flush();

    }

}
