SSLContext ctx = SSLContext.getInstance("TLS");
ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
SSLContext.setDefault(ctx);

// HttpsURLConnection
URL url = new URL(newUrl);
HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
connection.setRequestMethod("GET");

connection.setHostnameVerifier(new HostnameVerifier() {
    @Override
    public boolean verify(String arg0, SSLSession arg1) {
        return true;
    }
});

connection.connect();
BufferedReader reader = null;
String json;

// read the output from the server
reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
json = reader.readLine();
connection.disconnect();
