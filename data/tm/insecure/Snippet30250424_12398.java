public static void main(String args[]){ 
    System.setProperty("javax.net.ssl.keyStore", "C:/kei/tasks/MIP/Cert/ccc_acp.keystore");
    System.setProperty("javax.net.ssl.keyStorePassword", "password");
    MipCccSoapTest mipCccSoapTest = new MipCccSoapTest();
    mipCccSoapTest.testHttpConnection();        
}

private void testHttpConnection(){
    try{
        doTrustToCertificates();            
         URL url = new URL("https://10.7.3.43:9443/iboss/CustomerCareM1");
         HttpsURLConnection conn = (HttpsURLConnection)url.openConnection(); 
         HttpsURLConnection.getDefaultSSLSocketFactory();
         System.out.println("ResponseCoede ="+conn.getResponseCode());
    }catch(Exception ex){
        ex.printStackTrace();
    }
    System.exit(0);
    //end testing
}

// trusting all certificate 
 public void doTrustToCertificates() throws Exception {
    Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());
    TrustManager[] trustAllCerts = new TrustManager[]{
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    return;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
                    return;
                }
            }
    };

    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HostnameVerifier hv = new HostnameVerifier() {
        public boolean verify(String urlHostName, SSLSession session) {
            if (!urlHostName.equalsIgnoreCase(session.getPeerHost())) {
                System.out.println("Warning: URL host '" + urlHostName + "' is different to SSLSession host '" + session.getPeerHost() + "'.");
            }
            return true;
        }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(hv);
}
