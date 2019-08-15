public static JSONObject makeRequestCheck(String hostname, String token,String proId,String taskGrpId, String taskId) throws Exception {

    SSLSocketFactory sslFactory = new SimpleSSLSocketFactory(null);
    sslFactory
            .setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
    HttpParams params = new BasicHttpParams();
    HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
    HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
    SchemeRegistry registry = new SchemeRegistry();
    registry.register(new Scheme("http", PlainSocketFactory
            .getSocketFactory(), 80));
    registry.register(new Scheme("https", sslFactory, 443));
    ClientConnectionManager ccm = new ThreadSafeClientConnManager(params,
            registry);
    HttpClient client = new DefaultHttpClient(ccm, params);
    String hostnamee = hostname+"projects/"+proId+"/taskgroups/"+taskGrpId+"/tasks/"+taskId+"/complete";
    HttpPost httpost = new HttpPost(hostnamee);
    System.out.println(hostnamee);
//  StringEntity se = new StringEntity(jsonSignin.toString());

    HttpResponse response = null;
    try {
    //  httpost.setEntity(se);
        httpost.setHeader("X-HTTP-Method-Override", "PATCH");
        httpost.setHeader("Content-type", "application/json");
        httpost.setHeader("Accept", "*/*");
        httpost.setHeader("Accept-Encoding", "gzip, deflate");
        httpost.addHeader("Connection", "keep-alive");
        httpost.addHeader("Accept-Language", "en-GB,en-US;q=0.8,en;q=0.6");
        httpost.setHeader("User-Agent", "nnst");
        httpost.setHeader("Accept-Charset", "utf-8");
        if (token == "NV") {
        } else {
            httpost.setHeader("Authorization", "Token" + " " + token);
        }
        response = client.execute(httpost);

    } catch (Exception e) {
        System.out.println("::my Exception ::" + e);
    }
    if (response == null) {
        System.out.println("no data");
    }
    String output = EntityUtils.toString(response.getEntity());

    JSONObject jobj = new JSONObject(output);

    return jobj;
}
