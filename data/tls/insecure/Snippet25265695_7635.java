    //Using a client certificate
    String password = "clientpass";
    KeyStore keyStore = KeyStore.getInstance("PKCS12");
    InputStream is = context.getResources().openRawResource(R.raw.client);
    keyStore.load(is, password.toCharArray());
    is.close();
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
    kmf.init(keyStore, password.toCharArray());
    KeyManager[] keyManagers = kmf.getKeyManagers();


    // Using self signed certificate
    CertificateFactory cf = CertificateFactory.getInstance("X.509");
    is = context.getResources().openRawResource(R.raw.cacert);
    InputStream caInput = new BufferedInputStream(is);
    Certificate ca;
    try {
        ca = cf.generateCertificate(caInput);
        Log.i("CA","ca=" + ((X509Certificate) ca).getSubjectDN());
    } finally {
        caInput.close();
    }

    // Create a KeyStore containing our trusted CAs
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(null);
    trustStore.setCertificateEntry("ca", ca);

    // Create a TrustManager that trusts the CAs in our KeyStore
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    tmf.init(trustStore);
    TrustManager[] trustManagers = tmf.getTrustManagers();

    // Create an SSLContext that uses our Trustmanager and Keymanager
    SSLContext sslcontext = SSLContext.getInstance("TLS");

    sslcontext.init(keyManagers, trustManagers, null);

    //create a socket to connect with the server
    SSLSocketFactory socketFactory = sslContext.getSocketFactory();
    SSLSocket socket = (SSLSocket) socketFactory.createSocket(serverAddr, port);
    socket.setUseClientMode(true);
    socket.addHandshakeCompletedListener(this);
    socket.startHandshake();
