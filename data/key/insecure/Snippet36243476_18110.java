String associateTag = "example-20";
String awsAccessKeyId = "accessKeyId";
String awsSecretKey = "secretKey";
String endpoint = "webservices.amazon.com";
String uri = "/onca/xml";
String charset = "UTF8";

private String buildQueryString(String keywords) {
    Map<String,String> params = new ArrayMap<>();
    List<String> pairs = new ArrayList<>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

    params.put("Service","AWSECommerceService");
    params.put("Operation","ItemSearch");
    params.put("AWSAccessKeyId",awsAccessKeyId);
    params.put("AssociateTag",associateTag);
    params.put("SearchIndex","All");
    params.put("ResponseGroup","Images,ItemAttributes");
    params.put("Timestamp",sdf.format(new Date()));
    params.put("Keywords", keywords);

    Map<String, String> treeMap = new TreeMap<>(params);
    try {
        for (Map.Entry<String, String> param : treeMap.entrySet()) {
            pairs.add(URLEncoder.encode(param.getKey(), charset) + "=" + URLEncoder.encode(param.getValue(), charset));
        }
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    String queryString = "";
    for (int i = 0; i < pairs.size(); i++) {
        if (i != 0) {
            queryString += "&";
        }

        queryString += pairs.get(i);
    }

    Log.d(TAG, "queryString: " + queryString);

    return queryString;
}

private String buildSignature(String queryString) {
    String hash = "";

    try {
        String message = "GET\n" + endpoint + "\n" + uri + "\n" + queryString;
        Log.d(TAG, "message: " + message);

        Mac sha_HMAC = Mac.getInstance("HmacSHA256");

        SecretKeySpec secret_key = new SecretKeySpec(awsSecretKey.getBytes(charset), "HmacSHA256");
        sha_HMAC.init(secret_key);

        hash = Base64.encodeToString(sha_HMAC.doFinal(message.getBytes(charset)), Base64.DEFAULT);
    }
    catch (Exception e){
        System.out.println("Error");
    }

    return hash;
}

public void searchProducts(String keywords) {
    String requestUrl = "";
    String queryString = buildQueryString(keywords);
    String signature = buildSignature(queryString);

    Log.d(TAG, "signature: " + signature);

    try {
        requestUrl = "http://" + endpoint + uri + "?" + queryString + "&Signature=" + URLEncoder.encode(signature, charset);
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    Log.d(TAG, "requestUrl: " + requestUrl);

        Ion.with(context)
            .load(requestUrl)
            .asString()
            .setCallback(new FutureCallback<String>() {
                @Override
                public void onCompleted(Exception e, String result) {
                    Log.d(TAG, "searchProducts result: " + result);
                }
            });
}
