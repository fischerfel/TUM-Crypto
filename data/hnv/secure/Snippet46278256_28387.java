public class HttpsWithCert extends DefaultHttpClient {

    Context mContext;
    int keystoreResId;
    String keyStoreType;
    char[]  keyPassword;


    public HttpsWithCert(Context mContext,int keystoreResId,String keyStoreType,char[] keyPassword){
        this.mContext = mContext;
        this.keystoreResId = keystoreResId;
        this.keyStoreType = keyStoreType;
        this.keyPassword = keyPassword;

    }

    @Override
    protected ClientConnectionManager createClientConnectionManager() {
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme(HttpClientBuilderWithCert.HTTP_SCHEME, PlainSocketFactory.getSocketFactory(), HttpClientBuilderWithCert.HTTP_PORT));
        registry.register(new Scheme(HttpClientBuilderWithCert.HTTPS_SCHEME, newSslSocketFactory(), HttpClientBuilderWithCert.HTTPS_PORT));
        return new ThreadSafeClientConnManager(getParams(), registry);
    }

    private MySSLSocketFactory newSslSocketFactory() {
        try {
            KeyStore trusted = KeyStore.getInstance(keyStoreType);
            try {

                InputStream caInput = mContext.getResources().openRawResource(keystoreResId);

//                Certificate ca;
//                CertificateFactory cf = CertificateFactory.getInstance("X.509","BC");
//
//
//                try {
//                    ca = cf.generateCertificate(caInput);
//                    System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
//                } finally {
//                    caInput.close();
//                }

                // creating a KeyStore containing trusted CAs

                if (keyStoreType == null || keyStoreType.length() == 0) {
                    keyStoreType = KeyStore.getDefaultType();
                }

                trusted.load(caInput, keyPassword);
                String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
                tmf.init(trusted);
//                trusted.setCertificateEntry("ca", ca);
//                copyInputStreamToOutputStream(caInput, System.out);

//                trusted.load(null, null);

            } finally {
            }

            MySSLSocketFactory sslfactory = new MySSLSocketFactory(trusted);
            sslfactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
            return sslfactory;
        } catch (Exception e) {
            throw new AssertionError(e);
        }

    }

    private void copyInputStreamToOutputStream(InputStream in, PrintStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int c = 0;
        while ((c = in.read(buffer)) != -1) {
            out.write(buffer, 0, c);
        }
    }

    public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance(HttpClientBuilderWithCert.TLS);

        public MySSLSocketFactory(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
            super(truststore);

            TrustManager tm = new X509TrustManager() {
                @Override
                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };

            sslContext.init(null, new TrustManager[] {
                tm
            }, null);
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
