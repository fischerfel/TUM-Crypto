    InputStream is = null;
    String result = "";


    // http post
    try {

        HttpClient httpclient = getNewHttpClient();
        final String body = String
                .format("{\"block_face_line\" : [{\"longitude\" : -71.34345555,\"latitude\" : 42.7794343 },{\"longitude\" : -71.4473179666667,\"latitude\" : 42.7336227666667  },  {\"longitude\" : -71.4461721166667,\"latitude\" : 42.7321493333333  },{\"longitude\" : -71.4473179662267,\"latitude\" : 42.726227666667  } ],\"block_face_curb_side\" : \"LEFT\",\"block_face_collector_id\" : \"3\"}");
        HttpPost httppost = new HttpPost(
                "https://url.com");
        httppost.setHeader("Accept", "application/json");

        httppost.setHeader("Content-type",
                "application/json; charset=utf-8");

        httppost.setHeader("Content-length",
                Integer.toString(body.length()));

        httppost.addHeader(BasicScheme.authenticate(
                new UsernamePasswordCredentials("username", "password"),
                "UTF-8", false));

        httppost.setEntity(new StringEntity(body, "utf-8"));

        HttpResponse response = httpclient.execute(httppost);
        HttpEntity entity = response.getEntity();
        is = entity.getContent();

    } catch (Exception e) {
        Log.e("log_tag", "Error in http connection " + e.toString());
    }

    // convert response to string
    try {
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                is, "iso-8859-1"), 8);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
        is.close();
        result = sb.toString();
    } catch (Exception e) {
        Log.e("log_tag", "Error converting result " + e.toString());
    }

    // try parse the string to a JSON object

    Toast.makeText(getApplicationContext(), result, Toast.LENGTH_LONG)
            .show();
    Log.e("msg", result);

}



public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore
                    .getDefaultType());
            trustStore.load(null, null);

            SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory
                    .getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                    params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }
