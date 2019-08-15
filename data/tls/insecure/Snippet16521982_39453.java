  SSLSocketFactory sslFact = null;  
    SSLContext ctx;
    KeyStore ks;

    char[] passphrase = "hosttest".toCharArray();

    ks = KeyStore.getInstance("BKS");
    ks.load(SSLActivity.mContext.getResources().openRawResource(R.raw.hosttestcert), passphrase);

    TrustManagerFactory tmf =TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);

    ctx = SSLContext.getInstance("TLS");
    ctx.init(null,tmf.getTrustManagers(), null);
    sslFact = ctx.getSocketFactory();

    mySocket = (SSLSocket)sslFact.createSocket();
    mySocket.connect(new InetSocketAddress(InetAddress.getByName("127.0.0.1"), 7891),1000);
