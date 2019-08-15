SAXParserFactory spf = SAXParserFactory.newInstance();
  SAXParser sp = spf.newSAXParser();
  XMLReader xr = sp.getXMLReader();
  XMLmyHandler myHandler = new XMLmyHandler();
   xr.setContentHandler(myHandler);

            xr.parse(getInInputStreamFromURL(ur url here.....));


 public  AndroidHttpClient getClient(String userAgent) {
        HttpParams params = new BasicHttpParams();

        // Turn off stale checking. Our connections break all the time anyway,
        // and it's not worth it to pay the penalty of checking every time.
        HttpConnectionParams.setStaleCheckingEnabled(params, false);

        // Default connection and socket timeout of 20 seconds. Tweak to taste.
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);

        // Don't handle redirects -- return them to the caller. Our code
        // often wants to re-POST after a redirect, which we must do ourselves.
        HttpClientParams.setRedirecting(params, false);

        // Set the specified user agent and register standard protocols.
        HttpProtocolParams.setUserAgent(params, userAgent);
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        schemeRegistry.register(new Scheme("https", socketFactory, 443));
        ClientConnectionManager manager = new ThreadSafeClientConnManager(params, schemeRegistry);

        // We use a factory method to modify superclass initialization
        // parameters without the funny call-a-static-method dance.
        return new AndroidHttpClient(manager, params);
    }


 public InputStream getInInputStreamFromURL(String url) {
        InputStream inputStream = null;
        AndroidHttpClient httpClient = null;
        try {
            httpClient = getClient("Ramindu");
            // Example send http request
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet);
            inputStream = response.getEntity().getContent();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.e(TAG, "CAUGHT EXCEPTION : " + e);

        }
        return inputStream;
    }
