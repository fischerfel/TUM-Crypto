try {
    // Create a trust manager that does not validate certificate chains
    TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }

        public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
        }
    } };

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("SSL");
    sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Fetch url
    String url = "https://www.sos.nh.gov/corporate/soskb/SearchResults.asp?FormName=CorpNameSearch&Words=All&SearchStr=facebook&SearchType=Search";

    Connection.Response response = Jsoup //
            .connect(url) //
            .timeout(60000) //
            .method(Connection.Method.GET) //
            .userAgent("Mozilla/5.0 (Windows NT 10.0; WOW64; rv:41.0) Gecko/20100101 Firefox/41.0") //
            .execute();

    Document document = response.parse();
    System.out.println(document);
} catch (Exception e) {
    e.printStackTrace();
}
