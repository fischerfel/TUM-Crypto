private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
    JSONObject jsonObject = null;

    @Override
    protected void onPreExecute() {

        upload_image_progress.setProgress(0);

         int totalSize = 0
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Integer... progress) {
        // Making progress bar visible
        upload_image_progress.setVisibility(View.VISIBLE);
        // mHandler.sendEmptyMessageDelayed(progress[0], 100);
        // updating progress bar value
        upload_image_progress.setProgress(progress[0]);

        // updating percentage value
        // txtPercentage.setText(String.valueOf(progress[0]) + "%");
    }

    @Override
    protected String doInBackground(Void... params) {
        return uploadFile();
    }

    private String uploadFile() {

        String responseString = null;
        try {

            HostnameVerifier hostnameVerifier = org.apache.http.conn.ssl.SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER;

            HttpClient client = new DefaultHttpClient();

            SchemeRegistry registry = new SchemeRegistry();
            SSLSocketFactory socketFactory = SSLSocketFactory
                    .getSocketFactory();
            socketFactory
                    .setHostnameVerifier((X509HostnameVerifier) hostnameVerifier);

            registry.register(new Scheme("http", socketFactory, 443));
            SingleClientConnManager mgr = new SingleClientConnManager(
                    client.getParams(), registry);
            DefaultHttpClient httpClient = new DefaultHttpClient(mgr,
                    client.getParams());

            // Set verifier
            HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);


            HttpPost httpPost = new HttpPost("your url");


            AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                    new ProgressListener() {

                        @Override
                        public void transferred(long num) {
                            publishProgress((int) ((num * 100) / totalSize));

                        }
                    });

            File sourceFile = new File("image path");
            long fileSizeInBytes = sourceFile.length();

            // Convert the bytes to Kilobytes (1 KB = 1024 Bytes)
            long fileSizeInKB = fileSizeInBytes / 1024;

            // Convert the KB to MegaBytes (1 MB = 1024 KBytes)
            long fileSizeInMB = fileSizeInKB / 1024;
            // Log.e("file length in MB", "" + fileSizeInMB);
            if (fileSizeInMB > 2) {

                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;

                Bitmap bmp = BitmapFactory.decodeFile(image_uri, options);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bmp.compress(CompressFormat.JPEG, 70, bos);
                InputStream in = new ByteArrayInputStream(bos.toByteArray());

                ContentBody foto = new InputStreamBody(in, "image/jpeg",
                        image_uri);

                Log.e("size", "" + bos.size());

                entity.addPart("image_file", foto);


                totalSize = bos.size();
                Log.e("file length", "" + sourceFile.length());

                // Adding file data to http body
            } else {
                entity.addPart("image_file", new FileBody(sourceFile));
                totalSize = entity.getContentLength();

            }

            httpPost.setEntity(entity);

            HttpResponse response = client.execute(httpPost);

            // Making server call

            HttpEntity r_entity = response.getEntity();

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                // Server response
                responseString = EntityUtils.toString(r_entity);
                jsonObject = new JSONObject(responseString);
            } else {

                responseString = "Error occurred! Http Status Code: "
                        + statusCode;
            }

        } catch (ClientProtocolException e) {
            responseString = e.toString();
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
            responseString = e.toString();

        } catch (Exception e) {
            e.printStackTrace();

        }

        return responseString;

    }

    @Override
    protected void onPostExecute(String result) {
        try {

            if (jsonObject != null) {
                //get response here
                }
        super.onPostExecute(result);
    }

}
