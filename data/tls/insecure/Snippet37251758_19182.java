  KeyStore keyStore = KeyStore.getInstance("BKS");
        InputStream is = context.getResources().openRawResource(R.raw.bks);
        keyStore.load(is, "Password".toCharArray());
        is.close();


        //create a factory
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        //get context
        SSLContext mySSLContext = SSLContext.getInstance("TLS");

        //init context
        mySSLContext.init(
                null,
                trustManagerFactory.getTrustManagers(),
                new SecureRandom()
        );


        HostnameVerifier myHostnameVerifier = SSLSocketFactory.STRICT_HOSTNAME_VERIFIER;

        // set as an option
        IO.Options opts = new IO.Options();
        opts.sslContext = mySSLContext;
        opts.hostnameVerifier = myHostnameVerifier;
        socket = IO.socket("https://demo.in:9898", opts);
        socket.connect();
