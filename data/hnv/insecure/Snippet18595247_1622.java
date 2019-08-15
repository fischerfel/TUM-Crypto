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

                // e.printStackTrace();
                tryagain();

            } finally {
                // https.disconnect();
            }

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


            return stream;
        }
    }
