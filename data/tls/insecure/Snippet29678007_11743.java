        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }
    } };
    SSLContext sc = SSLContext.getInstance("SSL");

    HostnameVerifier hv = new HostnameVerifier() {

        public boolean verify(String arg0, SSLSession arg1) {
            return true;
        }
    };
    sc.init(null, trustAllCerts, new SecureRandom());

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(hv);
