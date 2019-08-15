private void execute() throws IllegalStateException, IOException, NoSuchAlgorithmException {

    Map<String, String> comment = new HashMap<String, String>();
    comment.put("accounts-groups", "customers/enterprise");
    comment.put("companyType", "customer");
    comment.put("companyName", "Test");
    String json = new GsonBuilder().create().toJson(comment, Map.class);
    Log.i(TAG, "json : "+json);

    HttpResponse response = makeRequest(URL, json);

    /*Checking response */
    if(response != null) {
        InputStream inputStream = response.getEntity().getContent(); //Get the data in the entity
        int statusCode = response.getStatusLine().getStatusCode();
        Log.i(TAG, "statusCode : "+statusCode);
        String result;
        // convert inputstream to string
        if(inputStream != null)
            result = convertStreamToString(inputStream);
        else
            result = "Did not work!";

        Log.i(TAG, "result : "+result);
    }
}

private HttpResponse makeRequest(String uri, String json) throws NoSuchAlgorithmException {
    Log.i(TAG, "uri : "+uri);
    try {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setEntity(new StringEntity(json, HTTP.UTF_8));

        long timestamp = System.currentTimeMillis();

        String signatureKey = PRIVATE_KEY + timestamp;

        byte[] bytesOfMessage = signatureKey.getBytes(HTTP.UTF_8);

        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] thedigest = md.digest(bytesOfMessage);
        char[] signature = Hex.encodeHex(thedigest);

        String finalSignature = String.valueOf(signature);

        Log.i(TAG, "finalSignature : "+finalSignature);

        httpPost.setHeader("Timestamp", ""+timestamp);
        httpPost.setHeader("Api_token", API_TOKEN);
        httpPost.setHeader("Signature" , finalSignature);

        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/json");          

        return new DefaultHttpClient().execute(httpPost);
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (ClientProtocolException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return null;
}
