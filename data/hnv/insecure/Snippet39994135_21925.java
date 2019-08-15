  AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            try {
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
                final String url = "https://192.168.1.13:8090/version";
                HttpPost httpPost = new HttpPost(url);
                HttpResponse response = httpClient.execute(httpPost);
                Log.i("","response is:" + response);
            }catch (Exception e){
                Log.e("","error trying to get:" + e.getMessage());
            }
            return null;
        }
    };
    asyncTask.execute();
