public static String getData()
 {  
    String str = "";
    try{
    TrustManager[] trustAllCerts = new TrustManager[] 
    {
       new X509TrustManager()
        {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() 
          {
            return null;
          }
          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }
          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }
        }
     };
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
         HostnameVerifier allHostsValid = new HostnameVerifier()
         {
            public boolean verify(String hostname, SSLSession session) 
             {
                           return true;
             }
         };
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
    URL url = new URL("My URL");
    URLConnection con = url.openConnection();
    Reader reader = new InputStreamReader(con.getInputStream());                
    while (true) 
    {
       int ch = reader.read();
       if (ch==-1) {
           break;
       }
       str += (char)ch;                                                                  
    }   }catch(Exception e){}
    return str;
