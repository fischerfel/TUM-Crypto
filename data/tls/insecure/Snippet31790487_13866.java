public static String httpsPost(String url, String body, String mediaType, String encoding) {
    SSLContext ctx;
    ctx = SSLContext.getInstance("TLS");
    ctx.init(new KeyManager[0], new TrustManager[]{new DefaultTrustManager()}, new SecureRandom());
    SSLContext.setDefault(ctx);
    HttpsURLConnection.setDefaultSSLSocketFactory(ctx.getSocketFactory());

    URL serverUrl = new URL(url);

    HttpsURLConnection con =  (HttpsURLConnection) serverUrl.openConnection();

    con.setRequestMethod("POST");
    con.setDoOutput(true);
    con.connect();

    OutputStreamWriter post = new OutputStreamWriter(con.getOutputStream());
    post.write(body);
    post.flush();

    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    String content = "";
    while ((inputLine = in.readLine()) != null) {
        content += inputLine;
    }
    post.close();
    in.close();

    return content;
}
