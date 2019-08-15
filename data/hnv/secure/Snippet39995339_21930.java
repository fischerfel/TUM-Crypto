 AsyncTask asyncTask = new AsyncTask() {
        @Override
        protected Object doInBackground(Object[] params) {
            HttpsURLConnection conn = null;
            try {
                URL url = new URL("https://192.168.1.13:8090/version");
                conn = (HttpsURLConnection) url.openConnection();
                conn.setSSLSocketFactory(SSLCertificateSocketFactory.getInsecure(0, null));
                conn.setHostnameVerifier(new AllowAllHostnameVerifier());
                conn.setRequestProperty("Accept-Charset", "UTF-8");
                conn.connect();
                InputStream response = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(response, "utf8"));
                StringBuffer sb = new StringBuffer();
                String line = "";

                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

                String data = sb.toString();
                Log.i("","response is:" + data);
            } catch (MalformedURLException e) {
                Log.e("","error trying to get conn:" + e.getMessage());
            } catch (IOException e) {
                Log.e("","error trying to get conn2:" + e.getMessage());
            }
            return null;
        }
    };
    asyncTask.execute();
