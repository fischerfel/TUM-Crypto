public void fetchLoginXML(){
    Log.d(TAG, "IN fetch ");
    HttpsURLConnection urlc;
    OutputStreamWriter out = null;
    DataOutputStream dataout;
    BufferedReader in = null;

    try {
        URL url = new URL(urlValuser);
        Log.d(TAG, "Final URL: " + url);
        urlc = (HttpsURLConnection) url.openConnection();
        urlc.setHostnameVerifier(new AllowAllHostnameVerifier());
        urlc.setRequestMethod("POST");

        urlc.setDoOutput(true);
        urlc.setDoInput(true);
        urlc.setUseCaches(false);
        urlc.setAllowUserInteraction(false);
        urlc.setRequestProperty("Content-Type", "text/xml");
        // perform POST operation
        Log.d(TAG, "Xml Source to POST: " + xmlsrc);
        String body = xmlsrc;
        OutputStream output = new BufferedOutputStream(urlc.getOutputStream());
        output.write(body.getBytes());
        output.flush();
        int responseCode = urlc.getResponseCode();
        in = new BufferedReader(new InputStreamReader(urlc.getInputStream()),8096);

        String response = "";

        String line = in.readLine();
        while (line != null) {
            response += line;
            line = in.readLine();
        }

        Log.d(TAG, "Post results Response Code " + responseCode);
        Log.d(TAG, "Post results Response " + response);

        in.close();

        factory = XmlPullParserFactory.newInstance();
        XmlPullParser myparser = factory.newPullParser();
        Log.d(TAG, "Setting myparser paramaters ");
        myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        Log.d(TAG, "Setting myparser input into xmldata ");
        myparser.setInput(new StringReader(response));
        Log.d(TAG, "send myparser to function parsexmlandstoreit ");
        parseXMLAndStoreIt(myparser);

    } catch (Exception e) {
        Log.e(TAG, "Error Posting Data: " + e.toString());
    } finally {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
