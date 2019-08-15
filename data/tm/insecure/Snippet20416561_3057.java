public class MyHttpClient extends DefaultHttpClient {
final Context context;
TrustManager easyTrustManager = new X509TrustManager() {
    @Override
    public void checkClientTrusted(
            X509Certificate[] chain,
            String authType) throws CertificateException {
    }

    @Override
    public void checkServerTrusted(
            X509Certificate[] chain,
            String authType) throws CertificateException {
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }    
};
  public MyHttpClient(Context context) {
    this.context = context;
  }

  @Override protected ClientConnectionManager createClientConnectionManager() {
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(
        new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
    registry.register(new Scheme("https", newSslSocketFactory(), 443));
    return new SingleClientConnManager(getParams(), registry);
  }


  private MySSLSocketFactory newSslSocketFactory() {
    try {
      KeyStore trusted = KeyStore.getInstance("BKS");      
      try {
         trusted.load(null, null);

      } finally {
      }

      MySSLSocketFactory sslfactory =  new MySSLSocketFactory(trusted);
        sslfactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        return sslfactory;
    } catch (Exception e) {
      throw new AssertionError(e);
    }

  }
  public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

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
   }
