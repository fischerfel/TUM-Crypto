class Login extends AsyncTask<String, String, Integer> {
    StringManipulator stringManipulator = new StringManipulator();
    String result = "";
    ParseSessionId parseSessionId = new ParseSessionId();

    @Override
    protected Integer doInBackground(String... args) {
        int statusCode;

        URLSet urlset = new URLSet();
        SoapLogin login = new SoapLogin();

        String username = "";
        String password = "";

        InputStream instream;
        try {
            HttpPost post = new HttpPost(new URI(urlset.getUrl()));

            post.setHeader("SOAPAction", urlset.getAction());
            post.setHeader("Content-Type", urlset.getContentType());

            Log.e("login string entity", login.getSoapLogin(args[0].trim(), args[1].trim()));

            post.setEntity(new StringEntity(login.getSoapLogin(args[0].trim(), args[1].trim())));

            KeyStore trusted = KeyStore.getInstance("BKS");
            trusted.load(null, "".toCharArray());
            SSLSocketFactory sslf = new SSLSocketFactory(trusted);
            sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(new Scheme("https", sslf, 443));
            SingleClientConnManager cm = new SingleClientConnManager(post.getParams(), schemeRegistry);
            CustomHttpClient customHttpClient = new CustomHttpClient();
            HttpClient client = customHttpClient.getNewHttpClient();

            HttpResponse response = client.execute(post);

            Log.e("response.getStatusLine()", "" + response.getStatusLine());
            statusCode = response.getStatusLine().getStatusCode();

            Header[] headers = response.getAllHeaders();
            for (Header h : headers) {
                Log.e("Reponse Header", h.getName() + ": " + h.getValue());
            }

            HttpEntity entity = response.getEntity();

            if (entity != null) {
                instream = entity.getContent();
                result = stringManipulator.convertStreamToString(instream);

                Log.e("result", result);
                sessionID = parseSessionId.parseSessionId(result);

                instream.close();
            }
            return statusCode;

        } catch (Exception e) {
            return null;
        }
    }

    @Override
    protected void onPostExecute(Integer resultCode) {
        if (resultCode == 200) {
        //  Toast.makeText(context, "Response: OK", Toast.LENGTH_LONG).show();

        } else if (resultCode == 400) {
            Toast.makeText(context, "Server Error: 400", Toast.LENGTH_LONG).show();

        } else if (resultCode == 500) {
            Toast.makeText(context, "Server Error: 500", Toast.LENGTH_LONG).show();

        } else {
            Toast.makeText(context, "Server Error: " + resultCode, Toast.LENGTH_LONG).show();
        }
    }
} 
