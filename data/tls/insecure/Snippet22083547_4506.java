 System.setProperty("jsse.enableSNIExtension", "false");  
 //Java 7 introduced SNI (enabled by default). The server I use is
 // misconfigured I suppose and
 // it sends an "Unrecognized Name" warning in the SSL handshake
 // which breaks my web service.

// Load CA from an InputStream (CA would be saved in Raw file,
// and loaded as a raw resource)
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    InputStream caInput = new BufferedInputStream(new FileInputStream("PATH_TO_CERT.crt"));

    Certificate ca;
    try {
        ca = cf.generateCertificate(caInput);
    } finally {
        caInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);
    keyStore.setCertificateEntry("ca", ca); 

    // Create a TrustManager that trusts the CAs in our KeyStore
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
    tmf.init(keyStore);

    // Create an SSLContext that uses our TrustManager
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);

 // Create all-trusting host name verifier 
    //  to avoid the following :
    //   java.security.cert.CertificateException: No name matching
    // This is because Java by default verifies that the certificate CN (Common Name) is
   // the same as host name in the URL. If they are not, the web service client fails.

     HostnameVerifier allHostsValid = new HostnameVerifier() {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    };

    //Install it
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);


    // Tell the URLConnection to use a SocketFactory from our SSLContext
    URL url = new URL("https....");
            urlConnection.setSSLSocketFactory(context.getSocketFactory());

    try {

        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");

        urlConnection.connect();

        switch(urlConnection.getResponseCode()){
        case 401:


            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getErrorStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line+"\n");
            }
            br.close();

            System.out.println( sb.toString());

        }



    } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (ProtocolException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }



}
