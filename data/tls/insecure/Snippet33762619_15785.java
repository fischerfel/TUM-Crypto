    URL url = new URL(postURL);
    HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
    SSLContext sc;
    sc = SSLContext.getInstance("TLS");

    sc.init(null, null, new java.security.SecureRandom());
    conn.setSSLSocketFactory(sc.getSocketFactory());

    String userpass = "bob" + ":" + "12345678";
    String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
    conn.setRequestProperty("Authorization", basicAuth);

    conn.setReadTimeout(7000);
    conn.setConnectTimeout(7000);
    conn.setRequestMethod("POST");
    conn.setDoInput(true);

    conn.connect();

    InputStream instream = conn.getInputStream();
    BufferedReader reader = new BufferedReader(new InputStreamReader(instream));

    StringBuilder everything = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
        everything.append(line);
    }

    JSONObject jsonObject = new JSONObject(everything.toString());

    return jsonObject;
