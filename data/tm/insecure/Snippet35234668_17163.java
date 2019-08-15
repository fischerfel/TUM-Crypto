TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        public void checkClientTrusted(X509Certificate[] certs, String authType) {
        }
        public void checkServerTrusted(X509Certificate[] certs, String authType) {
        }   
    }
};

// Install the all-trusting trust manager
SSLContext sc = SSLContext.getInstance("SSL");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

// Create all-trusting host name verifier
HostnameVerifier allHostsValid = new HostnameVerifier() {


    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public void check(String arg0, SSLSocket arg1) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void check(String arg0, X509Certificate arg1) throws SSLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void check(String[] arg0, SSLSocket arg1) throws IOException {
        // TODO Auto-generated method stub

    }

    @Override
    public void check(String[] arg0, X509Certificate arg1) throws SSLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void check(String arg0, String[] arg1, String[] arg2) throws SSLException {
        // TODO Auto-generated method stub

    }

    @Override
    public void check(String[] arg0, String[] arg1, String[] arg2) throws SSLException {
        // TODO Auto-generated method stub

    }
};

// Install the all-trusting host verifier
HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
