 private void trustCertify() throws IOException {
    final URL u = new URL(
            "https://10.10.16.136:9701/B001/static/faq_phone.html");
    HttpsURLConnection http = (HttpsURLConnection) u.openConnection();
    try {
        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }});
        SSLContext context = SSLContext.getInstance("SSL");
        context.init(null,
                new TrustManager[]{new CustomTrustManager()},
                new java.security.SecureRandom());

        HttpsURLConnection.setDefaultSSLSocketFactory(
                context.getSocketFactory());
    } catch (Exception e) { 
        e.printStackTrace();

    }
