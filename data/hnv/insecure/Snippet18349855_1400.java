System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
InputStream stream_content=null;
try
   {URL url=new URL("https://74.125.28.103/");
    HttpsURLConnection conn=(HttpsURLConnection)url.openConnection();
    conn.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER );
    conn.setDoOutput(true);
    conn.setRequestMethod("GET");
    conn.setRequestProperty("Host", "www.google.com");
    stream_content=conn.getInputStream();
   }
catch (Exception e) {}
