/*
 * Return the string from a authenticated SSL secured webserver call
 * */
public String sendHTTPSPostMessage(String userName, String userPass, String url, String[] postVars) throws NoSuchAlgorithmException, KeyManagementException, MalformedURLException, IOException {
    StringBuffer sb = new StringBuffer();

    final String serverAuth = userName + ":" + userPass;
    final String serverAuthBase64 = MyBase64.encode(serverAuth.getBytes());

    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, new TrustManager[] { new MyTrustManager() }, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(new MyHostnameVerifier());
    HttpsURLConnection con = (HttpsURLConnection) new URL(url).openConnection();

    try {
        StringBuffer urlParameters = new StringBuffer();
        String[] tmpPair = null;

        for (int i = 0; i < postVars.length; i++) {
            tmpPair = postVars[i].toString().split("=");

            if (i > 0)
                urlParameters.append("&" + tmpPair[0] + "=" + URLEncoder.encode(tmpPair[1], "UTF-8"));
            else
                urlParameters.append(tmpPair[0] + "=" + URLEncoder.encode(tmpPair[1], "UTF-8"));
        }

        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Basic " + serverAuthBase64);
        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        con.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.toString().getBytes().length));
        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);

        DataOutputStream wr = new DataOutputStream (con.getOutputStream());
        wr.writeBytes (urlParameters.toString());
        wr.flush();
        wr.close();

        BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream()), 8192 );
        String line;
        while ( ( line = br.readLine() ) != null ) {
                sb.append(line);
        }
    }
    catch(Exception e) {
        Log.e("sendHTTPSPostMessage", e.getLocalizedMessage());
    }
    finally {
        if(con != null) {
            con.disconnect(); 
        }
    }
    return sb.toString();
}
