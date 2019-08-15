class ProxyWorker implements Runnable {

    Socket client, server;
    OutputStream serverOut = null, clientOut = null;
    InputStream serverIn = null, clientIn = null;
    private static SSLSocketFactory sslSocketFactory;

    /**
     * Returns a SSL Factory instance that accepts all server certificates.
     * <pre>SSLSocket sock =
     *     (SSLSocket) getSocketFactory.createSocket ( host, 443 ); </pre>
     * @return  An SSL-specific socket factory.
     **/
    public static final SSLSocketFactory getSocketFactory() {
        if (sslSocketFactory == null) {
            try {
                TrustManager[] tm = new TrustManager[]{new NaiveTrustManager()};
                SSLContext context = SSLContext.getInstance("SSL");
                context.init(new KeyManager[0], tm, new SecureRandom());
                sslSocketFactory = (SSLSocketFactory) context.getSocketFactory();
            } catch (KeyManagementException e) {
                System.err.println("No SSL algorithm support: " + e.getMessage());
            } catch (NoSuchAlgorithmException e) {
                System.err.println("Exception when setting up the Naive key management.");
            }
        }
        return sslSocketFactory;
    }

    ProxyWorker(Socket c) {
        client = c;
        try {


            try {
                server = (SSLSocket) getSocketFactory().createSocket(Main.remoteH, Main.remotePort);
                ((SSLSocket)server).startHandshake();
                serverOut = server.getOutputStream();
                serverIn = server.getInputStream();
            } catch (Exception e) {
                System.err.println("SSL FAIL!\n" + e.toString());
                server = new Socket(Main.remoteH, Main.remotePort);
                serverOut = server.getOutputStream();
                serverIn = server.getInputStream();
            }

            clientOut = client.getOutputStream();
            clientIn = client.getInputStream();
        } catch (Exception e) {
            System.out.println("error\n" + e.toString());
            System.exit(0);
        }
    }

    public void run() {
        System.out.println("listening runner activated");
        try {
            byte[] b = new byte[0];
            BufferedOutputStream bos = new BufferedOutputStream(System.out);
            while (true) {
                int clientReady = clientIn.available();
                while (clientReady > 0) {
                    b = new byte[clientReady];
                    clientIn.read(b, 0, clientReady);
                    serverOut.write(b);
                    bos.write(b);
                    bos.flush();
                    clientReady = clientIn.available();
                }
                Thread.sleep(100);
                int serverReady = serverIn.available();
                while (serverReady > 0) {
                    b = new byte[serverReady];
                    serverIn.read(b, 0, serverReady);
                    clientOut.write(b);
                    bos.write(b);
                    bos.flush();
                    serverReady = serverIn.available();
                }
                if(server.isClosed()||client.isClosed()){
                    break;
                }
            }
        } catch (NullPointerException e) {
            System.err.println("NPE:\n"+e.getMessage());
            System.err.println(e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            try {
                server.close();
                client.close();
            } catch (IOException ex) {
                System.out.println("could not close sockets");
            }
        }
        System.out.println("Thread Exiting Now");
    }
}
