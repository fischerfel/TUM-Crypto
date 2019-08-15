public int postData(String usernamne, String password) {
    String url = "https://domainname.com/nclogin.submit";
    HttpPost httppost = new HttpPost(url);

    try {
        KeyStore trusted = null;
        try {
            trusted = KeyStore.getInstance("BKS");
        } catch (KeyStoreException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        trusted.load(null, "".toCharArray());
        MySSLSocketFactory sslf = null;
        try {
            sslf = new MySSLSocketFactory(trusted);
        } catch (KeyManagementException e) {
            Log.d(TAG, "Exception " + e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            Log.d(TAG, "Exception " + e);

            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (KeyStoreException e) {

            Log.d(TAG, "Exception " + e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

         nameValuePairs.add(new BasicNameValuePair("f_username", "myemail@address.com"));
         nameValuePairs.add(new BasicNameValuePair("f_passwd", "mypassword"));
         nameValuePairs.add(new BasicNameValuePair("f_method", "LOGIN"));

         httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

         SchemeRegistry schemeRegistry = new SchemeRegistry();
         schemeRegistry.register(new Scheme("https", sslf, 443));
         SingleClientConnManager cm = new SingleClientConnManager(httppost.getParams(), schemeRegistry);

         // NEW API WONT ALLOW THIS IN THE MAIN THREAD! hence ASYNC
         DefaultHttpClient client = new DefaultHttpClient(cm, httppost.getParams());

         HttpResponse result = client.execute(httppost);
         // Check if server response is valid
         StatusLine status = result.getStatusLine();
         Log.d(TAG, "STatus" + result.getStatusLine());
         if (status.getStatusCode() != 200) {
             throw new IOException("Invalid response from server: " + status.toString());
         }

         HttpEntity entity = result.getEntity();
         InputStream is = entity.getContent();
            if (is != null) {
                is.close(); // release connection
            }
            String phpsessid = "";
            // cookies from another blog
            // http://stackoverflow.com/questions/4224913/android-session-management
            List cookies = client.getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                Log.d(TAG, "no cookies received");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    // Log.d(TAG, "COOKIE-" + i + " " +
                    // cookies.get(i).toString());

                    if (cookies.get(i).toString().contains("PHPSESSID")) {
                        phpsessid = cookies.get(i).toString();
                        Log.d(TAG, "COOKIE FOR PHPSESSID - " + phpsessid);
                    }
                }
            } // end of blog

            entity.consumeContent();
            client.getConnectionManager().shutdown();

    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
    } catch (IOException e) {
        // TODO Auto-generated catch block
    } catch (NoSuchAlgorithmException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    } catch (CertificateException e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
    }

    return 1;
} // end of postData()

    public class MySSLSocketFactory extends SSLSocketFactory {
    SSLContext sslContext = SSLContext.getInstance("TLS");

    public MySSLSocketFactory(KeyStore truststore)
            throws NoSuchAlgorithmException, KeyManagementException,
            KeyStoreException, UnrecoverableKeyException {
        super(truststore);

        TrustManager tm = new X509TrustManager() {
            public void checkClientTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };

        sslContext.init(null, new TrustManager[] { tm }, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
            boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host,
                port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }
