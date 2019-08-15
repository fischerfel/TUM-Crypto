TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }

    @Override
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }
}};

// Install the all-trusting trust manager
SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, trustAllCerts, new java.security.SecureRandom());

SSLSocketFactory sslsocketfactory = sc.getSocketFactory();
SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket("google.com", 443);
...
