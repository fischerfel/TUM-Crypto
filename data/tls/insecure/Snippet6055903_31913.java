// Create a trust manager that does not validate certificate chains
TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager(){
    public X509Certificate[] getAcceptedIssuers(){return null;}
    public void checkClientTrusted(X509Certificate[] certs, String authType){}
    public void checkServerTrusted(X509Certificate[] certs, String authType){}
}};

// Install the all-trusting trust manager
try {
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
} catch (Exception e) {
    ;
}
