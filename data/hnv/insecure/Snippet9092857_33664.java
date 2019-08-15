public void postData() {

    // Add your data

    try {

       HttpPost post = new HttpPost(new URI("https://example.com"));


        KeyStore trusted = KeyStore.getInstance("BKS");
        trusted.load(null, "".toCharArray());
        SSLSocketFactory sslf = new SSLSocketFactory(trusted);
        sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme ("https", sslf, 443));
        SingleClientConnManager cm = new SingleClientConnManager(post.getParams(),
                schemeRegistry);

        HttpClient client = new DefaultHttpClient(cm, post.getParams());

        // Execute HTTP Post Request
        @SuppressWarnings("unused")
        HttpResponse result = client.execute(post);

    } catch (ClientProtocolException e) {
        // TODO Auto-generated catch block
        Log.e(TAG,e.getMessage());
        Log.e(TAG,e.toString());
        e.printStackTrace();
    } catch (IOException e) {
        // TODO Auto-generated catch block
        Log.e(TAG,e.getMessage());
        Log.e(TAG,e.toString());
        e.printStackTrace();
    } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
        Log.e(TAG,e.getMessage());
        Log.e(TAG,e.toString());
        e.printStackTrace();
        } catch (KeyStoreException e) {
            // TODO Auto-generated catch block
            Log.e(TAG,e.getMessage());
            Log.e(TAG,e.toString());
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            Log.e(TAG,e.getMessage());
            Log.e(TAG,e.toString());
            e.printStackTrace();
        } catch (CertificateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.e(TAG,e.toString());
            Log.e(TAG,e.getMessage());
        } catch (KeyManagementException e) {
            // TODO Auto-generated catch block
            Log.e(TAG,e.getMessage());
            Log.e(TAG,e.toString());
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            // TODO Auto-generated catch block
            Log.e(TAG,e.getMessage());
            Log.e(TAG,e.toString());
            e.printStackTrace();
        }
    }
