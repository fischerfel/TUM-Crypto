public String sendHttpsRequest(String httpsUrl, HttpMethod method, 
         String body, Map<String, String> headers) {

    String response = "";
    int responseCode = 0; 
    HttpsURLConnection conn = null;
    try {


        URL url = new URL(httpsUrl);
        conn = (HttpsURLConnection) url.openConnection();
        conn.setRequestMethod(method.toString());
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
        conn.setDoInput(true);
        conn.setDoOutput(true);
        conn.setUseCaches(false);
        conn.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });


        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, new TrustManager[]{
                new javax.net.ssl.X509TrustManager() {

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] chain,
                            String authType) throws CertificateException {
                    }
                }
        }, null);
        conn.setSSLSocketFactory(context.getSocketFactory());


        String param = body;
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(param);
        wr.flush();
        wr.close();
        responseCode = conn.getResponseCode();



        InputStream in = conn.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String line = "";

        while ((line = reader.readLine()) != null) {
            response += line;
        }
        reader.close();

    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if(conn != null) {
            conn.disconnect();
        }
    }

    return responseCode == 200?response:Integer.toString(responseCode);

}
