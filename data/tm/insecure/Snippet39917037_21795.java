public static Response execute(String url, Method method, String body) {
    Response response = new Response();

    try {
        ////////////////////////////////////////
         // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        } };
        // Install the all-trusting trust manager
        final SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };

        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        ////////////////////////////////////////


        URL urlObj = new URL(url);
        HttpsURLConnection conn = (HttpsURLConnection) urlObj.openConnection();

        conn.setInstanceFollowRedirects(false);
        conn.setRequestMethod(method.toString());
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "application/json");
        //conn.setRequestProperty("Authorization", _authToken);

        if (method == Method.POST || method == Method.PUT || method == Method.DELETE) {
            conn.setDoOutput(true);
            final OutputStream os = conn.getOutputStream();
            os.write(body.getBytes());
            os.flush();
            os.close();
        }

        int status = conn.getResponseCode();
        //log.info("HTTP request status code: "+status);
        InputStream is;
        if (status>399){
            is = conn.getErrorStream();
        }else{
            is = conn.getInputStream();
        }
        if (is==null) return null;
        BufferedReader rd = new BufferedReader(new InputStreamReader(is,
                "UTF-8"));

        String line;
        while ((line = rd.readLine()) != null) {
            response.body += line;
        }
        rd.close();

        response.statusCode = conn.getResponseCode();
        conn.disconnect();
    } catch (Exception e) {
        //log.error(e.getMessage());
        e.printStackTrace();
        System.out.println("");
        response.exception = e.getMessage();
    }
    return response;
}
