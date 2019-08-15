    KeyStore clientStore = KeyStore.getInstance("PKCS12");
    clientStore.load(new FileInputStream("/Users/me/authClient.p12"), "mypassword".toCharArray());

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(clientStore, "mypassword".toCharArray());
    KeyManager[] keyManagers = kmf.getKeyManagers();

    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(new FileInputStream("/Users/me/authClient-truststore.jks"), "mypassword".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    TrustManager[] tms = tmf.getTrustManagers();

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(keyManagers, tms, new SecureRandom());

    SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext);
    CloseableHttpClient httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();

    HttpGet httpget = new HttpGet(requestUrl);

    httpclient.execute(httpget);
