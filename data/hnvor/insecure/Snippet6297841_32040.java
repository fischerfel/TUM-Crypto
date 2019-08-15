    URL url = new URL("https://android.apis.google.com/c2dm/send");

    HostnameVerifier hVerifier = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession
                session) {
            return true;
        }
    };

    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    conn.setHostnameVerifier(hVerifier);
