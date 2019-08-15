    public void createConnection(){
     port =8888;
    listen = true;
            isSvrRuning = true;
            try {


                String KEYSTORE = Config.KEYSTORE_FILE;
                // String KEYSTORE = "/u04/app/ato/data/keystore/ATradSvrKeyStore";
                char[] KEYSTOREPW = "abcd".toCharArray();
                char[] KEYPW = "abcd".toCharArray();
                com.sun.net.ssl.TrustManagerFactory tmf;

                boolean requireClientAuthentication;

                java.security.Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
                java.security.KeyStore keystore = java.security.KeyStore.getInstance(
                        "JKS");
                keystore.load(new FileInputStream(KEYSTORE), KEYSTOREPW);

                com.sun.net.ssl.KeyManagerFactory kmf = com.sun.net.ssl.KeyManagerFactory.getInstance("SunX509");
                kmf.init(keystore, KEYPW);

                com.sun.net.ssl.SSLContext sslc = com.sun.net.ssl.SSLContext.getInstance("SSLv3");
                tmf = com.sun.net.ssl.TrustManagerFactory.getInstance("sunx509");
                tmf.init(keystore);

                sslc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
                SSLServerSocketFactory ssf = sslc.getServerSocketFactory();

                ssocket = (SSLServerSocket) ssf.createServerSocket();
                try {
                    ssocket.setReuseAddress(true);
                } catch (SocketException ex) {
                    loggerSvr.log(Level.SEVERE, "SocketException in setting timeout for serverSocket.");
                    ex.printStackTrace();
                }
                ssocket.bind(new InetSocketAddress(port));
                ssocket.setNeedClientAuth(true);
                while (listen) {
                    //wait for client to connect//
                    socket = ssocket.accept();
        // here goes the code which need to handle a new connection
    }

}catch(){
//there are several catch blocks to catch all checked exceptions
}
}
