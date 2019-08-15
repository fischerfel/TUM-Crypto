public class MyHttpClient1 extends DefaultHttpClient {

    final Context context;


    MyHttpClient1 mHttpClient=null;

    public MyHttpClient1(Context context) {
          this.context = context;
    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
      SchemeRegistry registry = new SchemeRegistry();
        KeyStore trustStore = null;
        try {
            trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        } catch (KeyStoreException e) {
            e.printStackTrace();  
        }
        try {
            trustStore.load(null, null);
        } catch (IOException e) {
            e.printStackTrace();  
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  
        } catch (CertificateException e) {
            e.printStackTrace();  
        }
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
      // Register for port 443 our SSLSocketFactory with our keystore
      // to the ConnectionManager
        try {
            registry.register(new Scheme("https",new MySSLSocketFactory(trustStore), 443));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();  
        } catch (KeyManagementException e) {
            e.printStackTrace();  
        } catch (KeyStoreException e) {
            e.printStackTrace();  
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();  
        }
        return new SingleClientConnManager(getParams(), registry);
    }

    public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("SSL");   //or TLS

    public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

}
