    public static void main(String args[]) {
    SSLSocket socket = null;
    X509TrustManager passthroughTrustManager = new X509TrustManager() {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    };

    try {
        SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
        sslContext.init(null, new TrustManager[] { passthroughTrustManager }, null);


        SSLSocketFactory ssf = sslContext.getSocketFactory();
        socket = (SSLSocket) ssf.createSocket(DEFAULT_HOST, DEFAULT_PORT);
        printSocketInfo(socket);

        socket.addHandshakeCompletedListener(new HandshakeCompletedListener() {

            @Override
            public void handshakeCompleted(HandshakeCompletedEvent arg0) {
                // TODO Auto-generated method stub
                System.err.println("handsakeCompleted:" + arg0.getSource().toString());
                flag = false;
            }

        });
        socket.startHandshake();

        peerCertificates = (X509Certificate[]) socket.getSession().getPeerCertificates();
        OutputStream out = socket.getOutputStream();

        // sendVersion(out);
        // sendAuthenticate(out);
        // sendPing(out);
        InputStream in = socket.getInputStream();

        byte[] buffer = new byte[1024 * 10];
        int d = -1;

        while ((d = in.read(buffer)) != -1) {// at mumble.main.SSLClient.main(SSLClient.java:97)
            System.out.println("rec[" + d + "]:" + new String(buffer));
        }

        System.out.println(d);

    } catch (Exception e) {
        System.err.println("Connection failed: " + e.toString());
        e.printStackTrace();
    } finally {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ioe) {
            }
            socket = null;
        }
    }

}

private static void printSocketInfo(SSLSocket s) {
    System.out.println("Socket class: " + s.getClass());
    System.out.println("   Remote address = " + s.getInetAddress().toString());
    System.out.println("   Remote port = " + s.getPort());
    System.out.println("   Local socket address = " + s.getLocalSocketAddress().toString());
    System.out.println("   Local address = " + s.getLocalAddress().toString());
    System.out.println("   Local port = " + s.getLocalPort());
    System.out.println("   Need client authentication = " + s.getNeedClientAuth());
    SSLSession ss = s.getSession();
    System.out.println("   Cipher suite = " + ss.getCipherSuite());
    System.out.println("   Protocol = " + ss.getProtocol());
}
