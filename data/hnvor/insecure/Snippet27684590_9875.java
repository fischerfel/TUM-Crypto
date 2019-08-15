System.setProperty("http.keepAlive", "false");
connection = (HttpURLConnection) endPoint.openConnection();
final String data = prepareRequestData();
final OutputStream os;

((HttpsURLConnection) connection).setHostnameVerifier(new HostnameVerifier() {

    @Override
    public boolean verify(String s, SSLSession sslSession) {
        return true;
    }
});
connection.setReadTimeout(val);
connection.setRequestMethod("POST");
connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
connection.setRequestProperty("Host", endPoint.getHost());
connection.setRequestProperty("Accept-Encoding", "gzip");
connection.setDoInput(true);
connection.setDoOutput(true);
os = connection.getOutputStream();
os.write(data.getBytes());
os.close();
