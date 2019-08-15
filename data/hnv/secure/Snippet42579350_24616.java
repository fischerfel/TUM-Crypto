hurlStack = new HurlStack() {
        @Override
        protected HttpURLConnection createConnection(URL url) throws IOException {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) super.createConnection(url);
            try {
                httpsURLConnection.setSSLSocketFactory(getSSLSocketFactory());
                httpsURLConnection.setHostnameVerifier(getHostnameVerifier());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return httpsURLConnection;
        }
    };
    MyVolley.init(this,hurlStack);
