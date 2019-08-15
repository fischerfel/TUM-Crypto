TrustManager[] trustAllCerts = new TrustManager[]{
    new X509TrustManager() {

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }
    }
};

SSLContext sslc = SSLContext.getInstance("TLS");
sslc.init(null, trustAllCerts, null);

SocketFactory sf = sslc.getSocketFactory();
SSLSocket s = (SSLSocket) sf.createSocket("127.0.0.1", 9124);
