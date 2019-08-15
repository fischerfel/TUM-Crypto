private void installCertificateVerificationBypassTools() {
    this.installCustomTrustManager();
    this.installCustomHostNameverifier();

}

private void installCustomTrustManager() {
    System.out.println("Installing custom trust manager");
    try {
        TrustManager[] nonDiscriminantTrustManager = new TrustManager[]{new X509TrustManager() {
            @Override
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                // ignore client trust check
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                // ignore server trust check
            }
        }};
        final SSLContext ret = SSLContext.getInstance("SSL");
        ret.init(null, nonDiscriminantTrustManager, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(ret.getSocketFactory());
    } catch (KeyManagementException | NoSuchAlgorithmException ex) {

    }
}

private void installCustomHostNameverifier() {
    System.out.println("Installing custom hostname verifier");
    HostnameVerifier hv = new HostnameVerifier() {
        @Override
        public boolean verify(String string, SSLSession ssls) {
            System.out.println("Verifying connection...");
            showSSLSessionDetails(ssls);
            if (ssls.getProtocol().contains("https")) {
                System.out.println("https session allowed.");
            }
            return true;
        }
    };
    HttpsURLConnection.setDefaultHostnameVerifier(hv);
}
