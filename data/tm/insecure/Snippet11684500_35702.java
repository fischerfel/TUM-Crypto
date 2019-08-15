private HttpsURLConnection createConnection(URL url) throws IOException{
HttpsURLConnection con=(HttpsURLConnection) url.openConnection();
con.setRequestMethod("POST");
con.setDoOutput(true);
con.setRequestProperty("Authorization", "Basic " + "**********");
con.setHostnameVerifier(new HostnameVerifier(){public boolean verify(String hostname, SSLSession session){return true;}});
TrustManager[] trustAllCerts=null;
SSLContext sslContext=null;
SSLSocketFactory sslSocketFactory;
try{
    trustAllCerts = new TrustManager[]{ new X509TrustManager(){
        public X509Certificate[] getAcceptedIssuers(){return null;}
        public void checkClientTrusted(X509Certificate[] chain, String authType){}
        public void checkServerTrusted(X509Certificate[] chain, String authType){}
    }};
    sslContext = SSLContext.getInstance( "SSL" );
    sslContext.init( null, trustAllCerts, new java.security.SecureRandom() );
    sslSocketFactory = sslContext.getSocketFactory();
    con.setSSLSocketFactory( sslSocketFactory );
    System.out.println("Response Code : " + con.getResponseCode());
    System.out.println("Cipher Suite : " + con.getCipherSuite());
    Certificate[] certs = con.getServerCertificates();
    for(Certificate cert : certs){
        System.out.println("Cert ext : "+cert);
        System.out.println("Cert Type : " + cert.getType());
        System.out.println("Cert Hash Code : " + cert.hashCode());
        System.out.println("Cert Public Key Algorithm : " + cert.getPublicKey().getAlgorithm());
        System.out.println("Cert Public Key Format : " + cert.getPublicKey().getFormat());
        System.out.println("\n");
    }
} catch (Exception e){e.printStackTrace();}
//printHTTPSCert(con);
return con;
}
