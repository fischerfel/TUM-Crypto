JAVA:

   String queryArgs = "command=returnBalances&nonce=" + nonce;
  System.out.println("queryArgs: " + queryArgs);  
    Mac shaMac = Mac.getInstance("HmacSHA512");
  System.out.println("shaMac: " + shaMac);  
    SecretKeySpec keySpec = new SecretKeySpec(secret.getBytes(), "HmacSHA512");
    shaMac.init(keySpec);
    final byte[] macData = shaMac.doFinal(queryArgs.getBytes());
  System.out.println("macData: " + macData);    
    String sign = Hex.encodeHexString(macData);
  System.out.println("sign: " + sign);    
    CloseableHttpClient httpClient = HttpClients.createDefault();
    HttpPost post = new HttpPost(url);
    post.addHeader("Key", key); 
    post.addHeader("Sign", sign);

    List<NameValuePair> params = new ArrayList<NameValuePair>();
    params.add(new BasicNameValuePair("command", "returnBalances"));
    //params.add(new BasicNameValuePair("command", "returnTicker"));
    params.add(new BasicNameValuePair("nonce", nonce));
    post.setEntity(new UrlEncodedFormEntity(params));
    System.out.println("post: " + post.toString());  
    System.out.println("params: " + params);   
    CloseableHttpResponse response = httpClient.execute(post);
    HttpEntity responseEntity = response.getEntity();
    System.out.println(response.getStatusLine());
    System.out.println(EntityUtils.toString(responseEntity));
