private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
    nameValuePairs.add(new BasicNameValuePair("thickness", params[0]));
    nameValuePairs.add(new BasicNameValuePair("roast", params[1]));
    nameValuePairs.add(new BasicNameValuePair("oil", params[2]));
    nameValuePairs.add(new BasicNameValuePair("number", params[3]));



        String url="https://192.168.0.100/testhttps.php";
        HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

        DefaultHttpClient client = new DefaultHttpClient();

        SchemeRegistry registry = new SchemeRegistry();
        SSLSocketFactory socketFactory = SSLSocketFactory.getSocketFactory();
        socketFactory.setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);
        registry.register(new Scheme("https", socketFactory, 443));
        SingleClientConnManager mgr = new SingleClientConnManager(client.getParams(), registry);
        DefaultHttpClient httpClient = new DefaultHttpClient(mgr, client.getParams());

        // Set verifier     
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);

        // Example send http request

        HttpPost httpPost = new HttpPost(url);
        try {

            HttpResponse response = httpClient.execute(httpPost);
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }
       }
            return "";
        }

        @Override
        protected void onPostExecute(String result) {

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
