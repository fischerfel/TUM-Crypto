public static void main(String[] args) throws Exception {
    Socket socket = new Socket("irc.chatspike.net",6667);
    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    String line;
    writer.write("STARTTLS\r\n");
    writer.flush(); 
    while ((line = reader.readLine()) != null) {
        if (line.contains(" 670 ")) {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return null;
                        }
                        public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                        }
                        public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                        }
                    }
                }, new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = ((SSLSocketFactory) sc.getSocketFactory());
            SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(
                                socket,
                                socket.getInetAddress().getHostAddress(),
                                socket.getPort(),
                                true);
            sslSocket.startHandshake();
        }
    }
