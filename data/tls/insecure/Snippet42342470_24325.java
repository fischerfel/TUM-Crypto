public static void main(String[] args) {
    boolean debug = true;

    System.out.println("Waiting For Connection");

    int intSSLport = 4444;

    {
        Security.addProvider(new Provider());
        //Security.addProvider(new BouncyCastleProvider());

        //System.setProperty("javax.net.ssl.keyStore","C:\\SSLCERT\\NEWAEDCKSSKYE");
        //System.setProperty("javax.net.ssl.keyStorePassword", "skyebank");
    }
    if (debug) {
        System.setProperty("javax.net.debug", "all");
    }
    FileWriter file = null;
    try {
        file = new FileWriter("C:\\SSLCERT\\Javalog.txt");

    } catch (Exception ee) {
        //message = ee.getMessage();

    }

    try {

        KeyStore keystore = KeyStore.getInstance("JKS");
        keystore.load(new FileInputStream("C:\\SSLCERT\\NEWAEDCKSSKYE"), "skyebank".toCharArray());
        file.write("Incoming Connection\r\n");

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory
                .getDefaultAlgorithm());
        kmf.init(keystore, "skyebank".toCharArray());

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(keystore);
        TrustManager[] trustManagers = tmf.getTrustManagers();

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(kmf.getKeyManagers(), trustManagers, null);

        SSLServerSocketFactory sslServerSocketfactory = (SSLServerSocketFactory) context.getServerSocketFactory();
        SSLServerSocket sslServerSocket = (SSLServerSocket) sslServerSocketfactory.createServerSocket(intSSLport);

        SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();
        SSLServerSocket server_socket = (SSLServerSocket) sslServerSocket;
        server_socket.setNeedClientAuth(true);

        sslSocket.startHandshake();

        System.out.println("Connection Accepted");
        file.write("Connection Accepted\r\n");

        while (true) {
            PrintWriter out = new PrintWriter(sslSocket.getOutputStream(), true);
            //BufferedReader in = new BufferedReader(new InputStreamReader(sslSocket.getInputStream()));
            String inputLine;

            //while ((inputLine = in.readLine()) != null) {
            out.println("Hello Client....Welcome");
            System.out.println("Hello Client....Welcome");
            //}

            out.close();
            //in.close();
            sslSocket.close();
            sslServerSocket.close();
            file.flush();
            file.close();
        }

    } catch (Exception exp) {
        try {
            System.out.println(exp.getMessage() + "\r\n");
            System.out.println(exp.getStackTrace() + "\r\n");
            file.write(exp.getMessage() + "\r\n");
            file.flush();
            file.close();
        } catch (Exception eee) {
            //message = eee.getMessage();
        }

    }

}

}
