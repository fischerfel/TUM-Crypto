public static void createTrustManager(){
    TrustManager[] trustAllCerts = createTrustAllCerts();

    // Install the all-trusting trust manager
    try{       
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, trustAllCerts, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
    catch (NoSuchAlgorithmException exception){}
    catch (KeyManagementException exception){}
}

public static TrustManager[] createTrustAllCerts() {
    // Create a trust manager that does not validate certificate chains
    X509TrustManager trustManager = createX509TrustManager();
    return new TrustManager[]{trustManager};
}

public static X509TrustManager createX509TrustManager() {
    X509TrustManager trustManager = new X509TrustManager(){
        public X509Certificate[] getAcceptedIssuers() { 

            return null; 

        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) { SSLUtilities.checkClientTrusted = true;}
        public void checkServerTrusted(X509Certificate[] certs, String authType) { SSLUtilities.checkServerTrusted = true;}
    };
    return trustManager;
}
