    @Override
    protected String doInBackground(String... params) {
        String response = null;

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }};

            final SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            };

            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                Log.d("TEST", "CONNECTION OK");
            } else {
                Log.d("TEST", "No network connection available.");
            }

            URL url = new URL("https://google.com");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            InputStream in = con.getInputStream();  // crashes here
            final Reader reader = new InputStreamReader(in);
            final BufferedReader r = new BufferedReader(reader);
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            response = total.toString();
            con.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
