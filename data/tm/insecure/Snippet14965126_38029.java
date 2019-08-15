...
            HttpURLConnection conn = null;
            DataOutputStream dataStream = null;


            //SETS COOKIE This should avoid the "Too many redirects issue" because It's apparently redirecting in an infinite loop because it's not maintain the user session.
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));


            if( webServiceUrl.contains("https") ){
                TrustManager[] trustAllCerts = new TrustManager[]{
                         new X509TrustManager() {
                             public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                                 return null;
                                 }
                             public void checkClientTrusted( java.security.cert.X509Certificate[] certs, String authType) {
                                 }
                             public void checkServerTrusted( java.security.cert.X509Certificate[] certs, String authType) {
                             }
                         }
                 }; // Install the all-trusting trust manager
                 try {
                     SSLContext sc = SSLContext.getInstance("TLS");
                     sc.init(null, trustAllCerts, new java.security.SecureRandom());
                     HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                     }
                 catch (Exception e) {
                     //L.l("TRUST MANAGER EXCEPTION");
                 }
            }

            try {

                responseInputStream = null;

                System.setProperty("http.keepAlive", "false");
                URL url = new URL(webServiceUrl);

                conn = (HttpURLConnection) url.openConnection();

                conn.setReadTimeout(Constants.CONNECTION_TIMEOUT_MILLISECONDS /* milliseconds */);
                conn.setConnectTimeout(Constants.CONNECTION_TIMEOUT_MILLISECONDS /* milliseconds */);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setDefaultUseCaches(false);

                if( useMultipart ){
                    conn.setRequestProperty("Content-Type", "multipart/form-data; boundary="+ Constants.MULTIPART_BOUNDARY);
                } else {
                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                }
                conn.setRequestProperty("Cache-Control", "no-cache");
                conn.connect();

                // DATA STREAM
                dataStream = new DataOutputStream(conn.getOutputStream());
  ...
