public WebSocketClient(Context context) throws Exception {
    super(new URI("wss://api.neelo.de"), new Draft_76());
    this.context = context;
    this.messageReceiver = messageReceiver;
    configureKeyStore();
  }

  void configureKeyStore() throws Exception {
    Log.d(TAG, "Configure key store");

    KeyStore ks = KeyStore.getInstance(STORE_TYPE);
    InputStream in = getContext().getResources().openRawResource(R.raw.android_keystore);

    ks.load(in, STORE_PASSWORD.toCharArray());
    KeyManagerFactory kmf = KeyManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    kmf.init(ks, STORE_PASSWORD.toCharArray());
    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(ks);

    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    SSLSocketFactory factory = sslContext.getSocketFactory();

    super.setSocket(factory.createSocket());
    }
