    try {
        .
        .
        .
        URL url = new URL(urlStr);
        Log.i(TAG,"[ URL ] " + urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        int size = conn.getContentLength();
        int responsecode = conn.getResponseCode();
        Log.d(TAG, "Responsecode: " + responsecode);
        .
        .
        .
        } catch (Exception e) {
        e.printStackTrace();
        }


private static void trustAllHosts() {

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {
            }
        } };

        try {
                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection
                                .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
                System.out.println("IOException : HTTPSRequest::trustAllHosts");
                e.printStackTrace();
        }
    }
