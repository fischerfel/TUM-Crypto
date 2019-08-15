    private ConnectionManager(MBoxConfiguration config) {
    try {
        this.config = config;


         try {
                trusted.load(in, "password".toCharArray());
        } finally {
            in.close();
        }

          SSLSocketFactory sf = new SSLSocketFactory(trusted);
          Socket socket;
          socket = ssf.createSocket();
      socket.connect(new InetSocketAddress(config.ip, config.port));
           KeyStore trusted = KeyStore.getInstance("BKS");

        InputStream in = MainActivity.context.getResources().openRawResource(R.raw.truststore);
        try {

            trusted.load(in, "password".toCharArray());
        } finally {
            in.close();
        }

        ssf = new SSLSocketFactory(trusted);
             ssf.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);                                                                                                                   


    } catch (Exception e) {

        e.printStackTrace();

    }

}
