    // AsyncTask to call web service
    public class NetworkTask extends AsyncTask<String, Integer, InputStream> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //
        }

        @Override
        protected InputStream doInBackground(String... params) {
            int result = 0;

            {
                Log.i("url...", Base_URL);

                try {
                    stream = getQueryResults(Base_URL);
                } catch (SocketTimeoutException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (SSLException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (IOException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (SAXException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                // The code below plays a ST Promo animation
                // prior to displaying update success or failure message
                for (int incr = 0; incr < 2; incr++) {
                    // Sleep for 1/2 second
                    // Invoke UI to change updating text to show 1 dot
                    // And Increasing the level to reduce the amount of clipping
                    // and
                    // slowly reveals the hand image
                    publishProgress(R.drawable.loading_full,
                            R.drawable.loading_empty, R.drawable.loading_empty,
                            R.drawable.loading_empty, R.drawable.loading_empty);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block

                    }
                    publishProgress(R.drawable.loading_full,
                            R.drawable.loading_full, R.drawable.loading_empty,
                            R.drawable.loading_empty, R.drawable.loading_empty);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block

                    }
                    publishProgress(R.drawable.loading_full,
                            R.drawable.loading_full, R.drawable.loading_full,
                            R.drawable.loading_empty, R.drawable.loading_empty);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block

                    }
                    publishProgress(R.drawable.loading_full,
                            R.drawable.loading_full, R.drawable.loading_full,
                            R.drawable.loading_full, R.drawable.loading_empty);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block

                    }
                    publishProgress(R.drawable.loading_full,
                            R.drawable.loading_full, R.drawable.loading_full,
                            R.drawable.loading_full, R.drawable.loading_full);

                    // Sleep for 1/2 second
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block

                    }
                }

                    return stream;
            }


        }
        /*
         * Sends a query to server and gets back the parsed results in a bundle
         * urlQueryString - URL for calling the webservice
         */
        protected synchronized InputStream getQueryResults(String urlQueryString)
                throws IOException, SAXException, SSLException,
                SocketTimeoutException, Exception {
            try {
                // HttpsURLConnection https = null;
                String uri = urlQueryString;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                BasicNameValuePair mdn1, mdn2,id1,id2;
                if (MDN.equals("")) {
                    mdn1 = new BasicNameValuePair("mdn1", null);
                    mdn2 = new BasicNameValuePair("mdn2", null);
                } else {
                    mdn1 = new BasicNameValuePair("mdn1", mdN1.toString());
                    mdn2 = new BasicNameValuePair("mdn2", mdN2.toString());

                }

                BasicNameValuePair car = new BasicNameValuePair("car", caR);
                if (ICCID.equals("")) {
                     id1 = new BasicNameValuePair("id1", null);
                     id2 = new BasicNameValuePair("id2", null);
                } else {
                     id1 = new BasicNameValuePair("id1",
                            iD1.toString());
                     id2 = new BasicNameValuePair("id2",
                            iD2.toString());
                }

                nameValuePairs.add(mdn1);
                nameValuePairs.add(mdn2);
                nameValuePairs.add(car);
                nameValuePairs.add(id1);
                nameValuePairs.add(id2);

                UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(
                        nameValuePairs, "ISO-8859-1");
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

                HttpClient httpClient = new DefaultHttpClient(ccm, params);
                params = httpClient.getParams();
                HttpClientParams.setRedirecting(params, true);

                HttpPost httpPost = new HttpPost(uri);
                httpPost.addHeader("Authorization",
                        getB64Auth("nmundru", "abc123"));

                httpPost.setHeader("Content-Type", "text/plain; charset=utf-8");

                Log.v("httpPost", httpPost.toString());

                httpPost.setEntity(urlEncodedFormEntity);
                HttpResponse httpResponse = httpClient.execute(httpPost);
                System.out.println("response...." + httpResponse.toString());
                Log.v("response...", httpResponse.toString());

                stream = httpResponse.getEntity().getContent();

                // save the InputStream in a file

                try {

                    FileOutputStream fOut = openFileOutput("settings.xml",
                            Context.MODE_WORLD_READABLE);

                    DataInputStream in = new DataInputStream(stream);
                    BufferedReader br = new BufferedReader(
                            new InputStreamReader(in));
                    String strLine;
                    while ((strLine = br.readLine()) != null) {
                         System.out.println(strLine); //to print the response
                        // in logcat
                        fOut.write(strLine.getBytes());

                    }
                    fOut.close();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fis = openFileInput("settings.xml");

            } catch (Exception e) {
                Log.e(LOG_TAG, e.toString());
                tryagain();

            } finally {
                // https.disconnect();
            }

            return stream;
        }

        private String getB64Auth(String login, String pass) {
            String source = login + ":" + pass;
            String ret = "Basic "
                    + Base64.encodeToString(source.getBytes(), Base64.URL_SAFE
                            | Base64.NO_WRAP);
            return ret;
        }
        @Override
        protected void onPostExecute(InputStream stream) {
            super.onPostExecute(stream);
            // This method is called to parse the response and save the
            // ArrayLists
            success();

        }

        public void success() {

            // to parse the response
            try {
                handler.getQueryResponse(fis);
            } catch (SAXException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            // set method to save the ArryaLists from the parser
            setArrayList();
            Intent i = new Intent(UpdateActivity.this, ConfigFinalActivity.class);
            startActivity(i);
            finish();

        }
        // Framework UI thread method corresponding to publishProgress call in
        // worker thread
        protected void onProgressUpdate(Integer... progress) {
            // Call function to update image view
            setProgressImgView(progress[0], progress[1], progress[2],
                    progress[3], progress[4]);
        }

    }

    private void setProgressImgView(Integer imgViewId1, Integer imgViewId2,
            Integer imgViewId3, Integer imgViewId4, Integer imgViewId5) {
        // update image view with the updating dots
        // Reset view layout in case orientation while updating
        setContentView(R.layout.updating);
        mProgressImageview1 = (ImageView) findViewById(R.id.loading_empty1);
        mProgressImageview2 = (ImageView) findViewById(R.id.loading_empty2);
        mProgressImageview3 = (ImageView) findViewById(R.id.loading_empty3);
        mProgressImageview4 = (ImageView) findViewById(R.id.loading_empty4);
        mProgressImageview5 = (ImageView) findViewById(R.id.loading_empty5);
        mProgressImageview1.setImageResource(imgViewId1);
        mProgressImageview2.setImageResource(imgViewId2);
        mProgressImageview3.setImageResource(imgViewId3);
        mProgressImageview4.setImageResource(imgViewId4);
        mProgressImageview5.setImageResource(imgViewId5);


    }

    @Override
    protected void onRestart() {
        super.onRestart();

        if (mErrorAlert != null)
            mErrorAlert.dismiss();
    }

    public void tryagain() {
        // Displaying final layout after failure of pre-ICS automatic settings
        // update
        setContentView(R.layout.tryagain);
        String tryAgainText = "";
        CharSequence styledTryAgainText;

        tryAgainText = String.format(getString(R.string.tryagain_text1),
                TotalSteps);
        styledTryAgainText = Html.fromHtml(tryAgainText);
        TextView tryAgain1 = (TextView) findViewById(R.id.tryagain_text1);
        tryAgain1.setText(styledTryAgainText);

        tryAgainText = String.format(getString(R.string.tryagain_text2),
                TotalSteps);
        styledTryAgainText = Html.fromHtml(tryAgainText);
        TextView tryAgain2 = (TextView) findViewById(R.id.tryagain_text2);
        tryAgain2.setText(styledTryAgainText);

        tryAgainText = String.format(getString(R.string.tryagain_text3),
                TotalSteps);
        styledTryAgainText = Html.fromHtml(tryAgainText);
        TextView tryAgain3 = (TextView) findViewById(R.id.tryagain_text3);
        tryAgain3.setText(styledTryAgainText);

    }
    private void assistUpdate() {
        // Displaying final layout after pre-ICS automatic settings update
        setContentView(R.layout.assist_update);
        assist_update_btn = (Button) findViewById(R.id.assist_update_btn);
        assist_update_btn.setOnClickListener((OnClickListener) this);

    }

    public void success() {

        // to parse the response
        try {
            handler.getQueryResponse(fis);
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // set method to save the ArryaLists from the parser
        setArrayList();
        Intent i = new Intent(this, ConfigFinalActivity.class);
        startActivity(i);
        finish();

    }

    public String getResult() {

        return result;
    }

}
