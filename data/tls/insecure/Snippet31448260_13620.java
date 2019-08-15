    char[]kspass = KEYSTOREPASS.toCharArray();
    char[]ctpass = KEYSTOREPASS.toCharArray();
    try {


        KeyStore ks = KeyStore.getInstance("PKCS12");
        //ks.load(new FileInputStream("file:///android_asset/singuler.keystore"),kspass);
        ks.load(getResources().getAssets().open("server.p12"),kspass);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks, ctpass);
        TrustManagerFactory tmFactory = TrustManagerFactory.getInstance("X509");
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(kmf.getKeyManagers(), null, null);

        //webServer.makeSecure(NanoHTTPD.makeSSLSocketFactory(ks, kmf.getKeyManagers()));
        webServerSSL.makeSecure(sc.getServerSocketFactory());

    } catch (Exception e) {
        // TODO: handle exceptionser
        Log.i("test", e.toString());
    }



    try {
        webServer.start(15);
        webServerSSL.start(15);
    } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
        webServer = null;
        webServerSSL=null;
        Log.i("test", e.toString());
    }
