char []passwKey = "passwd".toCharArray();
    KeyStore keyStore = KeyStore.getInstance("JKS");
    keyStore.load(new FileInputStream("abc.jks"), passwKey);
    KeyManagerFactory keyFac = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyFac.init(keyStore,passwKey);


    SSLContext sslContext = SSLContext.getInstance("SSL");
    sslContext.init(keyFac.getKeyManagers(), null, null);

    HttpsURLConnection conn = (HttpsURLConnection)new URL("https://url").openConnection();
    conn.setRequestMethod("GET");
    conn.setDoInput(true);
    conn.setSSLSocketFactory(sslContext.getSocketFactory());

    int responseCode = conn.getResponseCode();
    System.out.println("RESPONSE: " + responseCode);
