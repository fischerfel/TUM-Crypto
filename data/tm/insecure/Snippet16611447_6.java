public String GetWebPageHTTPS(String URI){
    TrustManager[] trustAllCerts = new TrustManager[] { 
            new X509TrustManager() {     
                public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
                    return null;
                } 
                public void checkClientTrusted( 
                    java.security.cert.X509Certificate[] certs, String authType) {
                    } 
                public void checkServerTrusted( 
                    java.security.cert.X509Certificate[] certs, String authType) {
                }
            } 
        }; 
        try {
            SSLContext sc = SSLContext.getInstance("SSL"); 
            sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (GeneralSecurityException e) {
        } 
        try { 
            System.out.println("URI: " + URI);
            URL url = new URL(URI); 
        } catch (MalformedURLException e) {
        } 
    BufferedReader read;
    URL inputURI;
    String line;
    String renderedPage = "";
    try{
        inputURI = new URL(URI);
        HttpsURLConnection connect;
        connect = (HttpsURLConnection)inputURI.openConnection();
        read = new BufferedReader (new InputStreamReader(connect.getInputStream()));
        while ((line = read.readLine()) != null)
            renderedPage += line;
        read.close();
    }
    catch (MalformedURLException e){
        e.printStackTrace();
    }
    catch (IOException e){
        e.printStackTrace();
    }
    return renderedPage;
}
