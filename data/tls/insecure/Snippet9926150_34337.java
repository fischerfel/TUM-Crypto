public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    try {

        SSLContext context = SSLContext.getInstance("TLS");                             

        KeyStore keyStore = KeyStore.getInstance("BKS");            
        keyStore.load(getResources().openRawResource(R.raw.clientkeystore), "xxx".toCharArray());                       

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("BKS");
        keyManagerFactory.init(keyStore, "xxx".toCharArray());
        KeyStore trustStore = KeyStore.getInstance("BKS");

        trustStore.load(getResources().openRawResource(R.raw.mytruststore), "Magnet1234".toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("BKS");
        trustManagerFactory.init(trustStore);            
        context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
    } catch (Exception e) {
        Log.e("", "", e);
    }
...
