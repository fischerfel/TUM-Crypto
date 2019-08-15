private static void trustAllHttpsCertificates() throws Exception {
//  Create a trust manager that does not validate certificate chains:
    javax.net.ssl.TrustManager[] trustAllCerts = new javax.net.ssl.TrustManager[1];
    javax.net.ssl.TrustManager tm = new YouNameClass();
    trustAllCerts[0] = tm; 
    javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, null);
    javax.net.ssl.HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
}
