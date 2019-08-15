//////////////////////////////////////////////////////////////////////////////////////
// this block of code turns off the certificate validation so the client can talk to an SSL
// server that uses a self-signed certificate
//
// !!!! WARNING make sure NOT to do this against a production site
//
// this block of code owes thanks to http://www.exampledepot.com/egs/javax.net.ssl/trustall.html
//

TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType){}

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType){}
    }
};

SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

//
//
// end of block of code that turns off certificate validation
// ////////////////////////////////////////////////////////////////////////////////////
