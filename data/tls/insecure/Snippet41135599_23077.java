public String callPostHttpsWebService(String webServiceName, JSONObject body, ArrayList<HeaderValue> headerValues, Context context) {
    try {
        HttpsURLConnection connection = Utils.getHttpsUrlConnection(context, webServiceName, headerValues);
        connection.setRequestMethod("POST");
        addJsonBodyToHttpsConnection(body, connection);
        connection.connect();
        BufferedReader r = new BufferedReader(new InputStreamReader(isResponse200(connection) ? connection.getInputStream() : connection.getErrorStream()));
        StringBuilder data = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null)
            data.append(line);
        return data.toString();
    } catch (Exception e) {
        e.printStackTrace();
    }
    return "";
}


public void addJsonBodyToHttpsConnection(JSONObject body, HttpsURLConnection connection) throws IOException {
     OutputStream os = connection.getOutputStream();
     BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
     writer.write(body.toString());
     writer.flush();
     writer.close();
    os.close();
}

public static HttpsURLConnection getHttpsUrlConnection(Context context, String webServiceName, ArrayList<HeaderValue> headerValues) {
    try {
        TrustManagerBuilder builder = new TrustManagerBuilder(context).or().useDefault();;
        SSLContext ssl = SSLContext.getInstance("TLS");
        ssl.init(null, builder.buildArray(), null);
        HttpsURLConnection connection = (HttpsURLConnection) new URL(webServiceName).openConnection();
        connection.setConnectTimeout(180000);
        connection.setSSLSocketFactory(ssl.getSocketFactory());
        connection.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String s, SSLSession sslSession) {
                return s.contains("poolmyride");
            }
        });
        for (HeaderValue value : headerValues)
            connection.setRequestProperty(value.name, value.value);
        return connection;
    } catch (Exception e) {
        e.printStackTrace();
    }

    return null;
    }
