URL url1;
try {
            url1 = new URL(url);

            if(url1.getProtocol().equalsIgnoreCase("https")){// you dont need this check
                try {
                HostnameVerifier hv = new HostnameVerifier() {

                       public boolean verify(String urlHostName, javax.net.ssl.SSLSession session) {

                           if (urlHostName.equals(session.getPeerHost())) {
                               logger.info("Verified HTTPS "+session.getPeerHost()+"  >> "+ urlHostName);
                           } else {
                               logger.info("Warning: URL host "+urlHostName+" is different to SSLSession host "+session.getPeerHost());
                           }
                           return true;
                       }
                   };

                   TrustManager[] trustAll = new javax.net.ssl.TrustManager[] { new javax.net.ssl.X509TrustManager() {

                       public java.security.cert.X509Certificate[] getAcceptedIssuers() {

                           return null;
                       }

                       public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {

                       }

                       public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {

                       }
                   } };

                   javax.net.ssl.SSLContext sc = javax.net.ssl.SSLContext.getInstance("SSL");

                    sc.init(null, trustAll, new java.security.SecureRandom());

                   SSLSocketFactory factory = (SSLSocketFactory) sc.getSocketFactory();
                   HttpsURLConnection.setDefaultSSLSocketFactory(factory);
                   HttpsURLConnection.setDefaultHostnameVerifier(hv);

                    HttpsURLConnection connection = (HttpsURLConnection)                 url1.openConnection();
                    connection.setDoOutput(true);
                    connection.setDoInput(true);
                    connection.setRequestMethod("POST");
                    connection.setUseCaches(false);     
