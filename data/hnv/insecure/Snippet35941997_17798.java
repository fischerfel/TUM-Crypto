        try {

        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        DefaultHttpClient client = new DefaultHttpClient();

        //Added my part

        final HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, 30000);
        client = new DefaultHttpClient(httpParams);

        //End my part

        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

        // Set verifier
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;

        // Checking http request method type
        if (method == POST) {
            HttpPost httpPost = new HttpPost(url);
            // adding post params
            if (params != null) {
                httpPost.setEntity(new UrlEncodedFormEntity(params));
            }

            httpResponse = httpClient.execute(httpPost);

        } else if (method == GET) {
            // appending params to url
            if (params != null) {
                String paramString = URLEncodedUtils
                        .format(params, "utf-8");
                url += String.format("?%s", paramString);
            }
            HttpGet httpGet = new HttpGet(url);

            httpResponse = httpClient.execute(httpGet);

        }
        httpEntity = httpResponse.getEntity();
        response = EntityUtils.toString(httpEntity, HTTP.UTF_8);

    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
