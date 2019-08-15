  InputStream in = new FileInputStream(new File("C:\\Program Files (x86)\\Java\\jre7\\bin\\my.keystore")); 
   KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType()); 
    ks.load(in, "blahblah".toCharArray()); 
in.close(); TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm()); 
    tmf.init(ks);
     X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0]; 
     SSLContext context = SSLContext.getInstance("TLS"); 
     context.init(null, new TrustManager[] {defaultTrustManager}, null); 
     javax.net.ssl.SSLSocketFactory sslSocketFactory = context.getSocketFactory();

URL url = new URL("https://emailer.driveclick.com/dbadmin/xml_post.pl"); 

     URLConnection con = url.openConnection(); 
    ((HttpsURLConnection) con).setSSLSocketFactory(sslSocketFactory); 
   ((HttpsURLConnection) con).setHostnameVerifier(new Verifier());
     con.connect(); 
    in = con.getInputStream();
