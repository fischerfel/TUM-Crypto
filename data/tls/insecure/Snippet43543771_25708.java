        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
                return myTrustedAnchors;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};


    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
        @Override
        public boolean verify(String hostname, SSLSession arg1) {
            if (hostname.equalsIgnoreCase("demo.mysite.com") ||
                    hostname.equalsIgnoreCase("prod.mysite.com") ||
                    hostname.equalsIgnoreCase("22.2.202.22:3333") ||
                    hostname.equalsIgnoreCase("cloud.cloudDeveSite.net") ||                            
                    hostname.equalsIgnoreCase("11.2.222.22:2222") ||
                    hostname.equalsIgnoreCase("multispidr.3rdPartyLibrary.io")) {
                return true;
            } else {
                return false;
            }
        }
    });
