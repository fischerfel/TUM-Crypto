public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
    try {

    SSLContext sslctx = null;       
        sslctx = SSLContext.getInstance("SSL");
    sslctx.init(null, new X509TrustManager[] { new MyTrustManager() }, null);

    res.setContentType("text/html");

    PrintWriter out = res.getWriter();

        HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
        URL url = new URL("https://xxxxx/yyyy.aspx");
    //URL url = new URL("https://google.com");

        HttpsURLConnection hc = (HttpsURLConnection) url.openConnection();

        hc.setConnectTimeout(3000);

        hc.setReadTimeout(5000);

        out.println("Response Code: " + hc.getResponseCode());
        hc.disconnect();

        out.close();

    }
    catch (Exception e) {
    e.printStackTrace();
    }
}
