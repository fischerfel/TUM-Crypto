public static void postFile(
    String host,
    String beaconId,
    String fileName,
    String base64File)
    throws IOException {

    StringBuilder request = new StringBuilder(base64File.length() + fileName.length() + 256);
    request.append("{\n");
    request.append("\"beaconId\": \"" + beaconId + "\",\n");
    request.append("\"fileName\": \"" + fileName + "\",\n");
    request.append("\"fileContent\": \"data:application/pdf;base64," + base64File + "\"\n");
    request.append("}");

    URL url = new URL("https://" + host + "/_ah/api/service/v1/files/add");
    //TODO ignore cert problems for now!
    TrustManager[] trustAllCerts = new TrustManager[] {
        new X509TrustManager() {

            public X509Certificate[] getAcceptedIssuers() {

                return null;
            }

            public void checkClientTrusted(
                X509Certificate[] certs,
                String authType) {}

            public void checkServerTrusted(
                X509Certificate[] certs,
                String authType) {}
        }
    };
    SSLContext sslContext;
    try {
        sslContext = SSLContext.getInstance("SSL");
    }
    catch (NoSuchAlgorithmException e) {
        throw new RuntimeException(e);
    }
    try {
        sslContext.init(null, trustAllCerts, new SecureRandom());
    }
    catch (KeyManagementException e) {
        throw new RuntimeException(e);
    }
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    try {
        conn.setSSLSocketFactory(sslContext.getSocketFactory());
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);
        try(OutputStream os = conn.getOutputStream()) {
            os.write(request.toString().getBytes("UTF-8"));
            os.flush();
            int respCode = conn.getResponseCode();
            String respMsg = conn.getResponseMessage();
            if (respCode != HTTP_CREATED) {
                throw new RuntimeException("HTTP error : " + conn.getResponseCode() + "\n" + respMsg);
            }
            String response = IOUtils.toString(conn.getInputStream(), "UTF-8");
            String[] lines = response.split("\n");
            if (lines.length != 6) {
                throw new RuntimeException("Invalid response : " + response);
            }
            if (!lines[2].contains("\"success\": true,")) {
                throw new RuntimeException("Post failed : " + response);
            }
        }
    }
    finally {
        conn.disconnect();
    }
} 
