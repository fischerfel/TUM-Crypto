public class Server {
    private final String dir;
    private final ServerSocket server;
    private final SSLServerSocket sslServer;

    public static String jarDir() {
        String uri = ClassLoader.getSystemClassLoader().getResource(".").getPath();
        try { return new File(URLDecoder.decode(uri,"UTF-8")).getPath()+File.separator; }
        catch (Exception e) { return null; }
    }

    private static SSLContext createSSLContext(String cert, char[] pass) throws Exception {
        /*//Load KeyStore in JKS format:
        KeyStore keyStore = KeyStore.getInstance("jks");
        keyStore.load(new FileInputStream(cert), pass);

        //Create key manager:
        KeyManagerFactory kmFactory = KeyManagerFactory.getInstance("SunX509");
        kmFactory.init(keyStore, pass); KeyManager[] km = kmFactory.getKeyManagers();

        //Create trust manager:
        TrustManagerFactory tmFactory = TrustManagerFactory.getInstance("SunX509");
        tmFactory.init(keyStore); TrustManager[] tm = tmFactory.getTrustManagers();

        //Create SSLContext with protocol:
        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        ctx.init(km, tm, null); return ctx;*/

        SSLContext ctx = SSLContext.getInstance("TLSv1.2");
        ctx.init(null, null, null); return ctx;
    }

    Server(String localPath, int port) throws Exception {
        this(localPath, port, 0);
    }

    //Server is being initialized with:
    //new Server("root", 80, 443);

    Server(String localPath, int port, int httpsPort) throws Exception {
        dir = localPath; File fdir = new File(jarDir(), dir);
        if(!fdir.isDirectory()) throw new Exception("No such directory '"+fdir.getAbsolutePath()+"'!");

        //Init Server:
        server = new ServerSocket(port);
        if(httpsPort > 0) {
            SSLContext ctx = createSSLContext("cert.jks", "pass".toCharArray());
            sslServer = (SSLServerSocket)ctx.getServerSocketFactory().createServerSocket(httpsPort);

            //TLS_DH_anon_WITH_AES_128_GCM_SHA256
            sslServer.setEnabledCipherSuites(new String[]{"TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256"});
            sslServer.setEnabledProtocols(new String[]{"TLSv1.2"});

            //Also does not work, same error:
            //sslServer.setEnabledCipherSuites(sslServer.getSupportedCipherSuites());
            //sslServer.setEnabledProtocols(sslServer.getSupportedProtocols());
        } else sslServer = null;

        /*new Thread(() -> { while(true) try {
            new HTTPSocket(server.accept(), this);
        } catch(Exception e) { Main.err("HTTP Server Error",e); }}).start();*/

        if(httpsPort > 0) new Thread(() -> { while(true) try {
            new HTTPSocket(sslServer.accept(), this);
        } catch(Exception e) { Main.err("HTTPS Server Error",e); }}).start();
    }

    /* ... Other Stuff ... */

}
