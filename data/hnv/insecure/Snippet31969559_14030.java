public String uploadPhoto(final File file) {


    Runnable runnable = new Runnable() {
        public void run() {


            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(uploadURL);

            httppost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
            String result = null;

            try {
                // Add your data
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                nameValuePairs.add(new BasicNameValuePair("file_name", file.getName()));
                nameValuePairs.add(new BasicNameValuePair("file_type", "image/jpg"));
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                // Execute HTTP Post Request
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity httpEntity = response.getEntity();
                result = EntityUtils.toString(httpEntity);
                int i = 1;

            } catch (ClientProtocolException e) {
                // TODO Auto-generated catch block
            } catch (IOException e) {
                // TODO Auto-generated catch block
            }
            if (result != null) {
                int putURLpos = result.indexOf(parseStringFragment);
                if (putURLpos != -1) {
                    putURL = result.substring(putURLpos + parseStringFragment.length(),
                            result.indexOf("\"", putURLpos + parseStringFragment.length()));

                    try {

                        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
                        trustStore.load(null, null);

                        SSLSocketFactory sf = (new MySSLSocketFactory(trustStore));
                        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                        BasicHttpParams httpParams = new BasicHttpParams();
                        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
                        HttpProtocolParams.setContentCharset(httpParams, HTTP.DEFAULT_CONTENT_CHARSET);
                        HttpProtocolParams.setUseExpectContinue(httpParams, true);

                        SchemeRegistry sr = new SchemeRegistry();
                        sr.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
                        sr.register(new Scheme("https", sf, 443));
                        ThreadSafeClientConnManager ccManager = new ThreadSafeClientConnManager(httpParams, sr);

                        HttpClient client = new DefaultHttpClient(ccManager, httpParams);

                        URI uri = new URI(putURL);
                        HttpPut put = new HttpPut(uri);
                        put.setHeader("Content-Type", "image/jpeg");
                        put.setHeader("User-Agent", "Apache-HttpClient/UNAVALIABLE (java 1.4)");

                        EntityBuilder eb = EntityBuilder.create();
                        eb.setFile(file);
                        put.setEntity(eb.build());
                        HttpResponse response = client.execute(put);


                        HttpEntity resEntity = response.getEntity();
                        String res = EntityUtils.toString(resEntity);

                        int i = 1;
                        i++;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    };
    Thread thread = new Thread(runnable);
    thread.start();
    try {
        thread.join();
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
    return uploadedPhotoURL;
}
