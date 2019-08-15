    //Load Two Keystores
    KeyStore keystore = KeyStore.getInstance("pkcs12", "SunJSSE");
    InputStream keystoreInput = new FileInputStream(cerPath);
    keystore.load(keystoreInput, passwd.toCharArray());
    System.out.println("Keystore has " + keystore.size() + " keys");

    // load the truststore, leave it null to rely on cacerts distributed with the JVM
    KeyStore truststore = KeyStore.getInstance("pkcs12", "SunJSSE");
    InputStream truststoreInput = new FileInputStream(cerPath);
    truststore.load(truststoreInput, passwd.toCharArray());
    System.out.println("Truststore has " + truststore.size() + " keys");

    //ssl context
    SSLContext sslcontext = SSLContext.getInstance("TLS");
    sslcontext.init(null, null, null);
    SSLSocketFactory sf = new SSLSocketFactory(keystore, passwd);
    Scheme https = new Scheme("https", 443, sf);

    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(https);
    ...
    client = new DefaultHttpClient(new PoolingClientConnectionManager(schemeRegistry));
    ...
    HttpResponse httpResponse = client.execute( targetHost, httpMethod);
