    try {
        TrustManagerFactory tmf;

        // local trust store
        tmf = TrustManagerFactory.getInstance("X509");
        tmf.init(loadLocalKeyStore(getApplicationContext()));

        // default trust store - works for https://www.google.com
        // tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        // tmf.init((KeyStore) null);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;
        URL u = new URL("https://10.0.2.2");

        HttpsURLConnection urlConnection = (HttpsURLConnection) u.openConnection();

        urlConnection.setSSLSocketFactory(context.getSocketFactory());
        urlConnection.setHostnameVerifier(hostnameVerifier);
        urlConnection.connect();

        System.out.println("Response Code: " + urlConnection.getResponseCode());
        System.out.println("Response Code: " + urlConnection.getCipherSuite());
    } 

    ...

    private KeyStore loadLocalKeyStore(Context context) {
        InputStream in = context.getResources().openRawResource(R.raw.newserverkeystore);
        KeyStore trusted = null;
        try {
           trusted = KeyStore.getInstance("BKS");
           trusted.load(in, "thisisasecret".toCharArray());
        } finally {
           in.close();
        }
       return trusted;
    }
