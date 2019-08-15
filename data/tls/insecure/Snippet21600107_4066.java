public static void main(String[] args) throws Exception {

    SSLContext sslctx = SSLContext.getInstance("SSL");
    sslctx.init(null, new X509TrustManager[] { new MyTrustManager() }, null);
    HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    });

    URL url = new URL("https://www.yahoo.com"); 
    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
    con.setRequestMethod("GET");
    con.connect(); //getting exception here
    if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
        BufferedReader br = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }
        br.close();
    } else{
        System.out.println("Not able to connect");
    }
    con.disconnect();
