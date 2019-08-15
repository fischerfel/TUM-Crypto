SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, new TrustManager[] { new TrustAllX509TrustManager() }, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
HttpsURLConnection.setDefaultHostnameVerifier( new HostnameVerifier(){
    public boolean verify(String string,SSLSession ssls) {
        return true;
    }
});
