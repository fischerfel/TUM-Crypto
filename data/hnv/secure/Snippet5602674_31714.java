private static String post(String urlString, Map formParameters)
throws MalformedURLException, ProtocolException, IOException {
    DataOutputStream ostream = null;

    trustAllHosts();
    URL tempUrl;
    StringBuffer buf = new StringBuffer();
    if(formParameters != null) {
        Set parameters = formParameters.keySet();
        Iterator it = parameters.iterator();
        //StringBuffer buf = new StringBuffer();

        for(int i = 0, paramCount = 0; it.hasNext(); i++) {
            String parameterName = (String) it.next();
            String parameterValue = (String) formParameters.get(parameterName);

            if(parameterValue != null) {
                parameterValue = URLEncoder.encode(parameterValue);
                if(paramCount > 0) {
                    buf.append("&");
                }
                buf.append(parameterName);
                buf.append("=");
                buf.append(parameterValue);
                ++paramCount;
            }
        }
        Log.v("smartdbhelper adding post parameters", buf.toString());


    }
    urlString = urlString + "?" + buf;
    Log.v("smartdbhelper url string", urlString);
    tempUrl = new URL(urlString);
    HttpsURLConnection https = (HttpsURLConnection) tempUrl.openConnection();
    https.setHostnameVerifier(DO_NOT_VERIFY);
    Log.v("smartdbhelper adding post parameters", https.toString());
    https.setRequestMethod("POST");
    https.setDoInput(true);
    https.setDoOutput(true);
    ostream = new DataOutputStream(https.getOutputStream());
    ostream.writeBytes(buf.toString());


if( ostream != null ) {
    ostream.flush();
        ostream.close();
    }
    Object contents = https.getContent();
    InputStream is = (InputStream) contents;
    StringBuffer buf2 = new StringBuffer();
    int c;
    while((c = is.read()) != -1) {
        buf2.append((char)c);
        Log.v("smartdbhelper bugger", buf2.toString());
    }
    https.disconnect();
    return buf2.toString();
}
