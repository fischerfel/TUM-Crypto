public static String makeInvalidHTTPSRequest(String url, String[] postVars, String userName, String userPass, Context ctx) throws MalformedURLException, IOException, NoSuchAlgorithmException, KeyManagementException {
    StringBuffer sb = new StringBuffer();
    String serverAuth = null;
    String serverAuthBase64 = null;
    StringBuffer urlParameters = new StringBuffer();
    InputStream rcvdInputStream = null;

    if (checkNetworkAvailability(ctx) == false) {
        GeneralMethods.writeLog("Network unavailable", 1, GeneralMethods.class);
        return null;
    }

    SSLContext sc = null;
    sc = SSLContext.getInstance("TLS");
    sc.init(null, new TrustManager[] { new KTPTrustManager() }, new SecureRandom());

    GeneralMethods.writeLog("makeInvalidHTTPSRequest-> " + url + ", " + userName + ", " + userPass, 0, GeneralMethods.class);

    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    HttpsURLConnection.setDefaultHostnameVerifier(new KTPHostnameVerifier());
    HttpsURLConnection con = null;
    con = (HttpsURLConnection) new URL(url).openConnection();

    if (userName != null) {
        serverAuth = userName + ":" + userPass;
        serverAuthBase64 = KTPBase64.encode(serverAuth.getBytes());
    }

    try {
        String[] tmpPair = null;
        con.setRequestMethod("POST");

        if (serverAuthBase64 != null)
            con.setRequestProperty("Authorization", "Basic " + serverAuthBase64);

        con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        if (postVars != null) {
            for (int i = 0; i < postVars.length; i++) {
                tmpPair = postVars[i].toString().split("=");

                if (i > 0)
                    urlParameters.append("&" + tmpPair[0] + "=" + URLEncoder.encode(tmpPair[1], "UTF-8"));
                else
                    urlParameters.append(tmpPair[0] + "=" + URLEncoder.encode(tmpPair[1], "UTF-8"));
            }
            con.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.toString().getBytes().length));
        }

        con.setUseCaches(false);
        con.setDoOutput(true);
        con.setDoInput(true);

        DataOutputStream wr = new DataOutputStream (con.getOutputStream());

        if (postVars != null)
            wr.writeBytes (urlParameters.toString());

        wr.flush();
        wr.close();

        if (con.getResponseCode() == 200) {
            globalRetries = 0;
            rcvdInputStream = con.getInputStream();
        }
        else if (con.getResponseCode() == 401) {
            con.disconnect();
            GeneralMethods.writeLog("error 401", 2, GeneralMethods.class);
            con = null;
            // SEND CONNECTION PROBLEM-INTENT
            return null;
        }
        else {
            GeneralMethods.writeLog("error - connection response code " + con.getResponseCode() + ": " + con.getResponseMessage() + " (length: "+ con.getContentLength() +")\n\n", 1, GeneralMethods.class);
            con.disconnect(); 
            con = null;
            // SEND CONNECTION PROBLEM-INTENT
            return null;
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(rcvdInputStream), 8192 );
        String line;
        while ( ( line = br.readLine() ) != null ) {
                sb.append(line);
        }
        con.disconnect();
        con = null;
    }
    catch(Exception e) {
        handleException(e, 2, GeneralMethods.class);
    }

    GeneralMethods.writeLog("makeInvalidHTTPSRequest: Response is \"" + sb.toString() + "\"\n\n", 0, GeneralMethods.class);

    if(con != null) {
        con.disconnect();
        con = null;
    }
    if (sb.toString().trim() == "")
        return null;
    else
        return sb.toString();
}
