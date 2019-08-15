public  Map<String,Object> pingSSLEnabledURL(String urlString) throws Exception {
        Map<String,Object> returnMap = null;
        // configure the SSLContext with a TrustManager
        SSLContext ctx = SSLContext.getInstance("TLS");
        ctx.init(new KeyManager[0], new TrustManager[] {
            new DefaultTrustManager() }, new SecureRandom());
        SSLContext.setDefault(ctx);

        URL url = new URL(urlString);
        HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
        conn.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        });
        returnMap = getResponseStatus(url, null, conn);

        conn.disconnect();
        return returnMap;
    }

 private  class DefaultTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    private  Map<String,Object> getResponseStatus(URL url, HttpURLConnection httpURLConnection,
            HttpsURLConnection httpsURLConnection) throws IOException {
        Map<String,Object> returnMap = new HashMap<String,Object>();

        returnMap.put("PROTOCOL", url.getProtocol());
        returnMap.put("PORT", url.getDefaultPort());
        returnMap.put("HOST", url.getHost());
        returnMap.put("URL", url.getFile()); 

        if (httpURLConnection != null){

            returnMap.put("STATUS_MESSAGE", httpURLConnection.getResponseMessage());
            returnMap.put("STATUS_CODE", httpURLConnection.getResponseCode());

        }
        if (httpsURLConnection != null){

            returnMap.put("STATUS_MESSAGE", httpsURLConnection.getResponseMessage());
            returnMap.put("STATUS_CODE", httpsURLConnection.getResponseCode());
        }

        return returnMap;

    }
