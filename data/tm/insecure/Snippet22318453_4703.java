public static X509TrustManager createTrustAllManager() {
    return new X509TrustManager() {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            // do nothing                   
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
            // do nothing
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    };
}
