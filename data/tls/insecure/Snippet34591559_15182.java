    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog = new ProgressDialog(LoginActivity.this);
        dialog.show();
    }

    @Override
    protected String doInBackground(Object[] params) {
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String DeviceId = telephonyManager.getDeviceId();

        try {


        HttpParams  client = new BasicHttpParams();
            final SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            schemeRegistry.register(new Scheme("https", createAdditionalCertsSSLSocketFactory(), 443));


            HttpProtocolParams.setVersion(client, HttpVersion.HTTP_1_1);




            ThreadSafeClientConnManager singleClientConnManager = new ThreadSafeClientConnManager(client,schemeRegistry);
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(singleClientConnManager,client);


        HttpPost httpPost = new HttpPost(URL);


            JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("device",DeviceId);
            jsonObject.accumulate("login", encryptedEmail);
            jsonObject.accumulate("password", encryptedPassword);

            StringEntity stringEntity = new StringEntity(jsonObject.toString());

            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = defaultHttpClient.execute(httpPost);
            int statusCode=      httpResponse.getStatusLine().getStatusCode();
            String response =  httpResponse.getEntity().getContent().toString();

        } catch (IOException e) {
            e.printStackTrace();
        }catch (Exception ex)
        {
            ex.printStackTrace();
        }





    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
        dialog.dismiss();

        startActivity(new Intent(LoginActivity.this, MainActivity.class));
    }
}






 protected org.apache.http.conn.ssl.SSLSocketFactory createAdditionalCertsSSLSocketFactory() {
        try {
            final KeyStore ks = KeyStore.getInstance("BKS");

            // the bks file we generated above
            final InputStream in = getResources().openRawResource( R.raw.analec);
            try {
                // don't forget to put the password used above in strings.xml/mystore_password
                ks.load(in, "password".toCharArray());
            } finally {
                in.close();
            }

            return new AdditionalKeyStoresSSLSocketFactory(ks);

        } catch( Exception e ) {
            throw new RuntimeException(e);
        }
    }


public class AdditionalKeyStoresSSLSocketFactory extends SSLSocketFactory {
    protected SSLContext sslContext = SSLContext.getInstance("TLS");

    public AdditionalKeyStoresSSLSocketFactory(KeyStore keyStore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException, UnrecoverableKeyException {
        super(null, null, null, null, null, null);
        sslContext.init(null, new TrustManager[]{new AdditionalKeyStoresTrustManager(keyStore)}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }



    /**
     * Based on http://download.oracle.com/javase/1.5.0/docs/guide/security/jsse/JSSERefGuide.html#X509TrustManager
     */
    public static class AdditionalKeyStoresTrustManager implements X509TrustManager {

        protected ArrayList<X509TrustManager> x509TrustManagers = new ArrayList<X509TrustManager>();


        protected AdditionalKeyStoresTrustManager(KeyStore... additionalkeyStores) {
            final ArrayList<TrustManagerFactory> factories = new ArrayList<TrustManagerFactory>();

            try {
                // The default Trustmanager with default keystore
                final TrustManagerFactory original = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                original.init((KeyStore) null);
                factories.add(original);

                for( KeyStore keyStore : additionalkeyStores ) {
                    final TrustManagerFactory additionalCerts = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    additionalCerts.init(keyStore);
                    factories.add(additionalCerts);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }



            /*
             * Iterate over the returned trustmanagers, and hold on
             * to any that are X509TrustManagers
             */
            for (TrustManagerFactory tmf : factories)
                for( TrustManager tm : tmf.getTrustManagers() )
                    if (tm instanceof X509TrustManager)
                        x509TrustManagers.add( (X509TrustManager)tm );


            if( x509TrustManagers.size()==0 )
                throw new RuntimeException("Couldn't find any X509TrustManagers");

        }

        /*
         * Delegate to the default trust manager.
         */
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            final X509TrustManager defaultX509TrustManager = x509TrustManagers.get(0);
            defaultX509TrustManager.checkClientTrusted(chain, authType);
        }

        /*
         * Loop over the trustmanagers until we find one that accepts our server
         */
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for( X509TrustManager tm : x509TrustManagers ) {
                try {
                    tm.checkServerTrusted(chain,authType);
                    return;
                } catch( CertificateException e ) {
                    // ignore
                }
            }
            throw new CertificateException();
        }

        public X509Certificate[] getAcceptedIssuers() {
            final ArrayList<X509Certificate> list = new ArrayList<X509Certificate>();
            for( X509TrustManager tm : x509TrustManagers )
                list.addAll(Arrays.asList(tm.getAcceptedIssuers()));
            return list.toArray(new X509Certificate[list.size()]);
        }
    }

}
