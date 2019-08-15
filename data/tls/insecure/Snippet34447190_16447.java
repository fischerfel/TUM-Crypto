private void testConnect() throws Exception{
    HttpURLConnection con = null;
    OutputStream httpsend = null;
    String url = "https://xxxxx/goldapi/rs/checkPaymentService";

    TrustManager[] trustall = new TrustManager[] { 
        new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
                return new X509Certificate[0];
            }
            public void checkClientTrusted( java.security.cert.X509Certificate[] certs, String authType) {}

            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
        } 
    }; 
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustall, new SecureRandom()); 
    HostnameVerifier allHostsValid = new HostnameVerifier() {
         @Override
         public boolean verify(String hostname, SSLSession session) {
             System.out.println("URL Host: " + hostname + " vs. " + session.getPeerHost());
             return true;
         }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    con = (HttpsURLConnection)(new URL(url).openConnection());
    con.setConnectTimeout(100000);

    // Set HTTP parameters for SOAP call
    //con.disconnect(); // test
    con.setAllowUserInteraction(true);
    con.setRequestMethod("POST");

    con.setDoOutput(true);
    con.setDoInput(true);
    con.setUseCaches(false);
    byte[] b = "test".getBytes();
    con.setRequestProperty( "Content-Length", String.valueOf( b.length));
    con.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset=UTF-8");
  //Send request to server
    httpsend = con.getOutputStream();
    httpsend.write(b);
    httpsend.flush();
    try{
        httpsend.close();
    }catch(Exception e){
        e.printStackTrace();
    }

}
