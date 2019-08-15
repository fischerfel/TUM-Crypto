// creating all trust manager. It will accept as valid any certificate.
TrustManager[] trustAllCerts = new TrustManager[]{
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
};
SSLContext sslContext = SSLContext.getInstance("SSL");
// set our friendly trust manager to ssl context. we pass it to connection later
sslContext.init(null, trustAllCerts, null); 
HttpsURLConnection connection = (HttpsURLConnection) (url).openConnection();
// set all trust manager to connection
connection.setSSLSocketFactory(sslContext.getSocketFactory());
// here we say not to check host name
connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return true;
            }
        });
