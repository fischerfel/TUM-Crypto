KeyStore clientStore = KeyStore.getInstance("PKCS12");
    clientStore.load(new FileInputStream("client.p12"), "password".toCharArray());

    KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    kmf.init(clientStore, "password".toCharArray());
    KeyManager[] kms = kmf.getKeyManagers();

    KeyStore trustStore = KeyStore.getInstance("JKS");
    trustStore.load(new FileInputStream("client.keystore"), "password".toCharArray());

    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(trustStore);
    TrustManager[] tms = tmf.getTrustManagers();

    SSLContext sslContext = null;
    sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kms, tms, new SecureRandom());

    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    URL url = new URL("https://cistest.apis-it.hr:8446/g2bservis");

    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

    String query = "<SendDocument></SendDocument>";
    con.setRequestMethod("POST");

    con.setRequestProperty("Content-Type","text");
    con.setDoOutput(true);
    con.setDoInput(true);

    DataOutputStream output = new DataOutputStream(con.getOutputStream());

    output.writeBytes(query);

    output.close();

    DataInputStream input = new DataInputStream( con.getInputStream() );

    for( int c = input.read(); c != -1; c = input.read() )
        System.out.print( (char)c );
    input.close();

    System.out.println("Resp Code:"+con .getResponseCode());
    System.out.println("Resp Message:"+ con .getResponseMessage());
