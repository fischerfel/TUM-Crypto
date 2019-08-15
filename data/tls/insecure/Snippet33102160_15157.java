static {
    try {
        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(null, new X509TrustManager[] {new AnyTrustManager()}, null);
        com.sun.net.ssl.internal.www.protocol.https.HttpsURLConnectionOldImpl.setDefaultSSLSocketFactory(ssl.getSocketFactory());            
    } catch (NoSuchAlgorithmException ex) {
        throw new Error(ex);
    } catch (KeyManagementException ex) {
        throw new Error(ex);
    }
}
