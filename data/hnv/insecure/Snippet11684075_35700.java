    public class CustomSSLSocketFactory extends SSLSocketFactory {

public static KeyStore rootCAtrustStore = null;
public static KeyStore clientKeyStore = null;
SSLContext sslContext = SSLContext.getInstance("TLS");
Context context;

/**
 *  Constructor.
 */
 public CustomSSLSocketFactory(Context context, KeyStore keystore, String    keyStorePassword, KeyStore truststore)
        throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {

    super(keystore, keyStorePassword, truststore);
    this.context = context;

    // custom TrustManager,trusts all servers
    X509TrustManager tm = new X509TrustManager() {

        public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
        return null;
        }
    };

Log.i("CLIENT CERTIFICATES", "Loaded client certificates: " + keystore.size());

// initialize key manager factory with the client certificate
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
    keyManagerFactory.init(keystore,null);

sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { tm }, null);
//sslContext.init(null, new TrustManager[]{tm}, null);
}

@Override
public Socket createSocket(Socket socket, String host, int port, boolean autoClose)  throws IOException, UnknownHostException {
   return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
}

@Override
public Socket createSocket() throws IOException {
   return sslContext.getSocketFactory().createSocket();
}

/**
 *  Create new HttpClient with CustomSSLSocketFactory.
 */
public static HttpClient getNewHttpClient(Context context, String alias,  X509Certificate[] chain, PrivateKey key) {
    try {
        // This is method from tutorial ----------------------------------------------------
        //The root CA Trust Store
        rootCAtrustStore = KeyStore.getInstance("BKS");
        rootCAtrustStore.load(null);

        //InputStream in = context.getResources().openRawResource(com.DII.RSS_Viewer.R.raw.rootca);
        //rootCAtrustStore.load(in, "PASSWORD".toCharArray());

        //The Keystore with client certificates.
        //clientKeyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        clientKeyStore = KeyStore.getInstance("pkcs12");
        clientKeyStore.load(null);

        // client certificate is stored in android's keystore
        if((alias != null) && (chain != null))
        {
            Key pKey = key;
            clientKeyStore.setKeyEntry(alias, pKey, "password".toCharArray(), chain);
        }

        //SSLSocketFactory sf = new CustomSSLSocketFactory(context, clientKeyStore, "password", rootCAtrustStore);
        SSLSocketFactory sf = new SSLSocketFactory(clientKeyStore, "password");

        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", (SocketFactory) sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

        return new DefaultHttpClient(ccm, params);
    } 
    catch (Exception e) 
    {
        return new DefaultHttpClient();
    }
    }
}
