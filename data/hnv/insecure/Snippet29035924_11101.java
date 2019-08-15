private class runBabyRun extends AsyncTask<Void, Integer, String> {


    final TextView temperature = (TextView) findViewById(R.id.mTemp4);
    // a few more TextViews here

    @Override
    protected String doInBackground(Void... params) {
        try {
            if (httpSelection.equals("http://")) {
                HttpClient httpClient = new DefaultHttpClient();
                // get url data
                HttpPost httppost = new HttpPost(weburi);
                HttpResponse response = httpClient.execute(httppost);
                HttpEntity entity = response.getEntity();
                webs = entity.getContent();
            }
            if (httpSelection.equals("https://")) {
                Log.e("log_tag", "Entered https if statement ");
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
                // get url data
                HttpPost httpPost = new HttpPost(weburi);
                HttpResponse response = httpClient.execute(httpPost);
                HttpEntity entity = response.getEntity();
                webs = entity.getContent();
            }
            // convert response to string
            try {
                final BufferedReader reader = new BufferedReader(
                        new InputStreamReader(webs, "iso-8859-1"),
                        8);
                // read one line of code, file is one whole string.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            //split file into array using space as delimiter
                            String clientraw = reader.readLine();
                            String[] parts = clientraw.split(" ");
                               clientRawData.addAll(Arrays.asList(parts));
                            //A few more setting up of fields here
                            // Get Weather Station Title
                            getSupportActionBar().setTitle(name(parts[32]));

                            temperature.setText(parts[4] + degrees);

                            time.setText(parts[29] + ":" + parts[30]);
                            date.setText(parts[74]);

                            webs.close();

                        } catch (Exception e) {
                            Log.e("log_tag", "Error in displaying textview " + e.toString());
                            e.printStackTrace();
                        }
                    }



                });

            } catch (Exception e) {
                Log.e("log_tag", "Error converting string " + e.toString());
            }
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());

            Toast.makeText(getApplicationContext(), "Error in Connection, please check your URL - " + weburi, Toast.LENGTH_LONG).show();
            // setup intent for Settings
            Intent intent = new Intent(MainActivity.this, Setting.class);
            // Launch the Settings Activity using the intent for result
            startActivityForResult(intent, UPDATE_WEBURL);
        }
        return null;
    }
