public class HTTPSServer {

    private int port = 9999;
    private boolean isServerDone = false;

    public static void main(String[] args) {
        HTTPSServer server = new HTTPSServer();
        server.run();
    }

    HTTPSServer() {
    }

    HTTPSServer(int port) {
        this.port = port;
    }

    // Create the and initialize the SSLContext
    private SSLContext createSSLContext() {
        try {

            //Returns keystore object in definied type, here jks
            KeyStore keyStore = KeyStore.getInstance("JKS");
            //loads the keystore from givin input stream, and the password to unclock jks
            keyStore.load(new FileInputStream("x509-ca.jks"), "password".toCharArray());

            // Create key manager
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "password".toCharArray());
            KeyManager[] km = keyManagerFactory.getKeyManagers();

            // Create trust manager
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);
            TrustManager[] tm = trustManagerFactory.getTrustManagers();

            // opens a secure socket with definied protocol
            SSLContext sslContext = SSLContext.getInstance("TLSv1.2");

            //System.out.println(keyStore.getCertificate("root").getPublicKey());
            //System.out.println(keyStore.isKeyEntry("root"));
            sslContext.init(km, tm, null);

            return sslContext;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    }

    // Start to run the server
    public void run() {
        SSLContext sslContext = this.createSSLContext();

        try {
            // Create server socket factory
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();

            // Create server socket
            SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(this.port);

            System.out.println("SSL server started");
            while (!isServerDone) {
                SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

                // Start the server thread
                new ServerThread(sslSocket).start();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Thread handling the socket from client
    static class ServerThread extends Thread {

        private SSLSocket sslSocket = null;

        ServerThread(SSLSocket sslSocket) {
            this.sslSocket = sslSocket;
        }

        public void run() {
            sslSocket.setEnabledCipherSuites(sslSocket.getSupportedCipherSuites());
            //System.out.println("HIER: " + sslSocket.getHandshakeSession());

            //Klappt nicht, auch nicht, wenn der Client diese Zeile ebenfalls besitzt
            //sslSocket.setEnabledCipherSuites(new String[]{"TLS_ECDH_ECDSA_WITH_AES_128_CBC_SHA256"});
            try {
                // Start handshake
                sslSocket.startHandshake();

                // Get session after the connection is established
                SSLSession sslSession = sslSocket.getSession();
                System.out.println(sslSession.getPeerHost());
                System.out.println(sslSession.getLocalCertificates());
                System.out.println("\tProtocol : " + sslSession.getProtocol());
                System.out.println("\tCipher suite : " + sslSession.getCipherSuite());
                System.out.println("\tSession context : " + sslSession.getSessionContext());
                //System.out.println("\tPeer pricipal of peer : " + sslSession.getPeerPrincipal());

                // Start handling application content
                InputStream inputStream = sslSocket.getInputStream();
                OutputStream outputStream = sslSocket.getOutputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(outputStream));

                String line = null;

                String[] suites = sslSocket.getSupportedCipherSuites();
                for (int i = 0; i < suites.length; i++) {
                    //System.out.println(suites[i]);
                    //System.out.println(sslSession.getCipherSuite());
                }

                while ((line = bufferedReader.readLine()) != null) {
                    System.out.println("Inut : " + line);

                    if (line.trim().isEmpty()) {
                        break;
                    }
                }

                // Write data
                printWriter.print("HTTP/1.1 200\r\n");
                printWriter.flush();

                sslSocket.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
