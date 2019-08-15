 private class PostTask extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mBT.setEnabled(false);
        }

        @Override
        protected String doInBackground(String... data) {

            OkHttpClient client;// = new OkHttpClient();
            client = getUnsafeOkHttpClient();
            client.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "amount=10&orderRefNum=110&storeId=xxxx&postBackURL=https://www.jeevaysehat.com/");
            Request request = new Request.Builder()
                    .url("https://easypaystg.easypaisa.com.pk/easypay/Index.jsf")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .build();
            Response response = null;
            String resp = null;
            try {
                response = client.newCall(request).execute();
                resp = response.body().string();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //return resp;
            return response.request().url().toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("data", s);
            try {
                mBT.setEnabled(true);
                String[] ist = s.split("=");
                String[] snd = ist[1].split("&");
                Token = snd[0];

                Log.e("token", Token);
                Log.e("posturl", ist[2]);

                pburl = ist[2];
                medPost.setText(pburl);
                medtoken.setText(Token);


                //  Log.e("pburl", pburl);
               /* Intent ii = new Intent(MainActivity.this, Payment_details.class);
                ii.putExtra("data", token);
                 startActivity(ii);*/
                //http://jeevaysehat.com/?auth_token=260915100358342650147434472217522869797&postBackURL=http%3A%2F%2Fjeevaysehat.com%2F


            } catch (Exception e) {

            }
        }
    }

    private class PostTask1 extends AsyncTask<String, String, String> {
        String mtoken;
        String PBURL;

        public PostTask1(String token, String pb) {
            mtoken = token;
            PBURL = pb;
        }

        @Override
        protected String doInBackground(String... data) {


            OkHttpClient client;// = new OkHttpClient();
            client = getUnsafeOkHttpClient();
            client.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
            RequestBody body = RequestBody.create(mediaType, "auth_token=" + mtoken + "&postBackURL=https://www.jeevaysehat.com/");
            Request request = new Request.Builder()
                    .url("https://easypaystg.easypaisa.com.pk/easypay/Confirm.jsf")
                    .post(body)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    .build();

            Response response = null;
            String resp = null;
            try {
                response = client.newCall(request).execute();
                resp = response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return response.request().url().toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("data", s);
//here i redirect to webview activity
            Intent ii = new Intent(MainActivity.this, Payment_details.class);
            ii.putExtra("data", s);
            startActivity(ii);
            // Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
            // startActivity(browserIntent);

        }
    }
