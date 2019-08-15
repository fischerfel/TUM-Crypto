        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 10000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 10000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;
        DefaultHttpClient client = new DefaultHttpClient(httpParameters);
        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);

        HttpClient httpclient = new DefaultHttpClient(mgr, client.getParams());
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);


        HttpPost httpPost = new HttpPost(URLinString);


        json = jsonObject.toString();
        System.out.println("input json is::" + json);
        StringEntity se = new StringEntity(json);
        httpPost.setEntity(se);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");


        HttpResponse httpResponse = null;

        httpResponse = httpclient.execute(httpPost);

        String result = EntityUtils.toString(httpResponse.getEntity());
