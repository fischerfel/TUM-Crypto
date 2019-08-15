            KeyStore trustStore = KeyStore.getInstance("JKS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            InputStream trustStoreStream = getBaseContext().getResources().openRawResource(R.raw.mytruststore);
            trustStore.load(trustStoreStream, "test1234".toCharArray());
            trustManagerFactory.init(trustStore);

            // Setup keystore
            KeyStore keyStore = KeyStore.getInstance("JKS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            InputStream keyStoreStream = getBaseContext().getResources().openRawResource(R.raw.mykeystore);
            keyStore.load(keyStoreStream, "test1234".toCharArray());
            keyManagerFactory.init(keyStore, "test1234".toCharArray());

            SSLSocketFactory sslf = new SSLSocketFactory(keyStore);
            sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme ("https", sslf, 443));
            SingleClientConnManager cm = new
            SingleClientConnManager(post.getParams(), schemeRegistry);

            HttpClient client = new DefaultHttpClient(cm, post.getParams());
            HttpResponse result = client.execute(post); 
