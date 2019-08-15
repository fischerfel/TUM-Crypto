public class MySSLSocketFactory extends SSLSocketFactory {
        SSLContext sslContext = SSLContext.getInstance("TLS");

        @SuppressWarnings("deprecation")
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

    @SuppressWarnings("deprecation")
    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

HttpClient httpclient = getNewHttpClient();
            HttpPost httppost = new HttpPost("https://wwww.api.com/index.php/login");
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder
            .addTextBody("username", username, ContentType.TEXT_PLAIN)
            .addTextBody("password", password, ContentType.TEXT_PLAIN);

            HttpEntity httpEntity = builder.build();
            try{
                httppost.setEntity(httpEntity);
                System.out.println("executing request " + httppost.getRequestLine());
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity resEntity = response.getEntity();

                System.out.println("status :"+response.getStatusLine());
                if (resEntity != null) {
                    jsonResponse = EntityUtils.toString(resEntity);
                    Log.d(TAG, "-------------->> " + jsonResponse);
                    resEntity.consumeContent();
                } else {
                    System.out.println("Response entity is null");
                    Log.d(TAG, "Response Entity is NULL");
                }
                httpclient.getConnectionManager().shutdown();
                return jsonResponse;
            }catch(Exception e){
                return "";

            }
