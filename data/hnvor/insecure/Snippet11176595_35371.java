HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    conn.setHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    });
