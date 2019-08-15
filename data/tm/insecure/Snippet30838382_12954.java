TLSClientParameters tcp = new TLSClientParameters();
tcp.setDisableCNCheck(true);
// Creating Trust Manager
TrustManager[] trustAllCerts = new TrustManager[] {
    new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] certs, String authType) {
        }
} };

tcp.setTrustManagers(trustAllCerts);
conduit.setTlsClientParameters(tcp);

HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
httpClientPolicy.setAllowChunking(false);
httpClientPolicy.setAccept("*/*");
httpClientPolicy.setContentType("text/xml;charset=UTF-8");
httpClientPolicy.setHost(url.split("/")[2]);
conduit.setClient(httpClientPolicy);
