public static String sendPOSTRequest(String URL, String data) throws Exception {
    String req = "sendPOSTRequest(" + URL.replace("https://www.somefakeurl.com", "") + ")";
    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    SSLContext sslctx = SSLContext.getInstance("SSL");

    //See below for information on TmN...
    sslctx.init(null, new X509TrustManager[] { new TmN() }, null);

    HttpsURLConnection.setDefaultSSLSocketFactory(sslctx.getSocketFactory());
    URL url = new URL(URL);

    HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
    con.setRequestProperty("User-Agent",
            "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
    // con.setRequestProperty("Content-Type",
    // "application/x-www-form-urlencoded");
    con.setRequestMethod("POST");
    con.setDoOutput(true);

    // Basically just makes sure the data given is URLEncoded...
    if (data != null && !StringUtils.isWhitespace(data)) {
        String[] array0 = data.split("&");
        for (int i = 0; i < array0.length; i++) {
            String[] array1 = array0[i].split("=");
            try {
                array1[1] = URLEncoder.encode(array1[1], "UTF-8");
                array0[i] = String.join("=", array1);
            } catch (Exception e) {
                System.out.println("Error on: " + array0[i]);
                e.printStackTrace();
            }
        }
        data = String.join("&", array0);
    }

    OutputStream wr = con.getOutputStream();
    wr.write(data.getBytes());

    con.connect();
    if (con.getResponseCode() == HttpsURLConnection.HTTP_OK) {
        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String line;
        while ((line = br.readLine()) != null) {
            baos.write(line.getBytes());
        }
        br.close();
    }
    con.disconnect();

    //Print and return the request
    String res = new String(baos.toByteArray());
    req += (" = " + res + "\n");
    System.out.println(req);
    return res;
}
