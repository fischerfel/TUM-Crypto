    public HttpsURLConnection getTolerantClient() {
    HttpsURLConnection client = new HttpsURLConnection();
    SSLSocketFactory sslSocketFactory = (SSLSocketFactory) client.getConnectionManager().getSchemeRegistry().getScheme("https").getSocketFactory();
    final X509HostnameVerifier delegate = sslSocketFactory.getHostnameVerifier();
    if(!(delegate instanceof MyVerifier)) {
        sslSocketFactory.setHostnameVerifier(new MyVerifier(delegate));
    }
    return client;
}
