E.log("establishing connection: trying to create context");

SSLContext context = SSLContext.getInstance("TLS");
context.init(null, new X509TrustManager[]{new X509TrustManager(){
        public void checkClientTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {}
        public void checkServerTrusted(X509Certificate[] chain,
                        String authType) throws CertificateException {}
        public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
        }}}, new SecureRandom());

E.log("establishing connection: trying to create socket factory");

SSLSocketFactory factory = (SSLSocketFactory) context.getSocketFactory ();

E.log("establishing connection: trying to create socket");

kkSocket = (SSLSocket) 
        factory.createSocket(host, Integer.parseInt(port));

E.log("establishing connection: trying to create out writer");
out = new PrintWriter(kkSocket.getOutputStream(), true);

E.log("establishing connection: trying to create in reader");                   
in = new BufferedReader(new InputStreamReader
           (kkSocket.getInputStream()));
