TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    public void checkClientTrusted(X509Certificate[] certs, String authType) {
    }
    public void checkServerTrusted(X509Certificate[] certs, String authType) {
    }
} };

// Install the all-trusting trust manager
final SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
SSLConnectionSocketFactory sslConnectionFactory = new SSLConnectionSocketFactory(sc, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

// Create all-trusting host name verifier
X509HostnameVerifier allHostsValid = new X509HostnameVerifier() {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }

    public void verify(String arg0, SSLSocket arg1) throws IOException {
        //Not Required Auto-generated method stub from X509HostnameVerifier

    }

    public void verify(String arg0, X509Certificate arg1) throws SSLException {
        //Not Required Auto-generated method stub from X509HostnameVerifier

    }

    public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {
        //Not Required Auto-generated method stub from X509HostnameVerifier

    }
};
CloseableHttpClient httpclient = HttpClients.custom()
        .setSSLSocketFactory(sslConnectionFactory)
        .setHostnameVerifier(allHostsValid)
        .build();
