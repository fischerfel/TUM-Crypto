public void testSimpleHttpsClient() throws CertificateException, InterruptedException, UnrecoverableKeyException, NoSuchAlgorithmException,
        IOException, KeyManagementException, KeyStoreException {


    URL url = new URL("https://intstg1-kaakioskpublicapi.ptstaging.ptec/TLE/36171/player/info");

    HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
    con.setRequestMethod( "GET" );

    SSLContext sslContext = SSLContext.getInstance("TLS");

    char[]  passphrase = "storepass".toCharArray();
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(this.getClass().getResourceAsStream("clienttrust.jks"), passphrase);
    System.out.println(ks);

    TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
    tmf.init(ks);

    System.out.println(tmf.toString());

    HostnameVerifier hostnameVerifier = new HostnameVerifier() {

        public boolean verify(String s, SSLSession sslSession) {
            return s.equals(sslSession.getPeerHost());
        }
    };
    con.setHostnameVerifier(hostnameVerifier);

    sslContext.init(null, tmf.getTrustManagers(), null);
   con.setSSLSocketFactory(sslContext.getSocketFactory());

    int responseCode = con.getResponseCode();
    InputStream inputStream;
    if (responseCode == HttpURLConnection.HTTP_OK) {
        inputStream = con.getInputStream();
    } else {
        inputStream = con.getErrorStream();
    }

    // Process the response
    BufferedReader reader;
    String line = null;
    reader = new BufferedReader( new InputStreamReader( inputStream ) );
    while( ( line = reader.readLine() ) != null )
    {
        System.out.println( line );
    }

    inputStream.close();
}
