    public HttpURLConnection getHTTPConnection(String urlString, String username, String password) throws IOException, KeyManagementException, NoSuchAlgorithmException {
    trustAllHttpsCertificates();
    HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
           return true;
        }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(hv);
    URL url = new URL(urlString);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();

    if( username != null && password != null ) {
        String userPassword = username + ":" + password;
        String encoding = DatatypeConverter.printBase64Binary(userPassword.getBytes("UTF-8"));
        conn.setRequestProperty( "Authorization", "Basic " + encoding );
    }
    conn.setDoInput(true);
    conn.setDoOutput(true);
    conn.setAllowUserInteraction(true);
    conn.setConnectTimeout(DEFAULT_HTTP_TIMEOUT);
    return conn;
}

protected static void trustAllHttpsCertificates() throws NoSuchAlgorithmException, KeyManagementException {
    javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
    javax.net.ssl.TrustManager tm = new miTM();
    trustAllCerts[0] = tm;
    javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, null);
    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
}

public static class miTM implements javax.net.ssl.TrustManager, javax.net.ssl.X509TrustManager {
    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
        return null;
    }
    public boolean isServerTrusted(java.security.cert.X509Certificate[] certs) {
        return true;
    }
    public boolean isClientTrusted(java.security.cert.X509Certificate[] certs) {
        return true;
    }
    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType)
        throws java.security.cert.CertificateException {
        return;
    }
    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType)
        throws java.security.cert.CertificateException {
        return;
    }
}
