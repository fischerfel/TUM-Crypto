HttpURLConnection conn = null;
            final HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
           };
            try {
                URL url = new URL(YOUR_URL);
                if (url.getProtocol().toLowerCase().equals("https")) {
                    trustAllHosts();
                    HttpsURLConnection https = (HttpsURLConnection) url.openConnection();
                    https.setHostnameVerifier(DO_NOT_VERIFY);
                    conn = https;
                } else {
                    conn = (HttpURLConnection) url.openConnection();
                }
                conn.setInstanceFollowRedirects(false);
                conn.connect();
        String location = conn.getHeaderField( "User name" );
