public JSONObject jhttpcon(JSONObject jObj, String sGSTWsUrl,String sStateCode,StringBuffer sBfResCode) {
  String sProxyIp="";
  String sProxyPort="";
  String sResFromGst= "";
  int iResCode=0;
    
  try {
   sProxyIp= getGSTProxyServer();
   sProxyPort= getGSTProxyPort();
   System.out.println("-------sProxyIp--" + sProxyIp);
   System.out.println("-------sProxyPort--" + sProxyPort);
   
   //CloseableHttpClient httpclient = HttpClients.createDefault();
   ![when i remove this comment it is giving me protocol version error]
   HttpHost proxy = new HttpHost(sProxyIp, Integer.parseInt(sProxyPort), "http");
   RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
      
   SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
         sslContext.init(null, null, new SecureRandom());
         SSLSocketFactory sf = new SSLSocketFactory(sslContext);
         Scheme httpsScheme = new Scheme("https", 443, sf);
         SchemeRegistry schemeRegistry = new SchemeRegistry();
         schemeRegistry.register(httpsScheme);
         ClientConnectionManager cm =  new SingleClientConnManager(schemeRegistry);
         HttpClient httpclient = new DefaultHttpClient(cm);
   
   
         HttpGet httpReq = new HttpGet(sGSTWsUrl);
          System.out.println("-----util--sGSTWsUrl--@@@" + sGSTWsUrl);
             httpReq.setConfig(config);
             httpReq.addHeader(IGBMGSTConstants.GST_CONTENT_TYPE,getGSTContentType());
       httpReq.addHeader(IGBMGSTConstants.GST_CLIENT_ID,getGSTClientId());
       httpReq.addHeader(IGBMGSTConstants.GST_STATE_CODE,sStateCode);
       httpReq.addHeader(IGBMGSTConstants.GST_USER_NAME,getGSTUserName());
       httpReq.addHeader(IGBMGSTConstants.GST_PWD,getGSTPwd());
       httpReq.addHeader(IGBMGSTConstants.GST_CLIENT_SCRT,getGSTClientSecret());
            // System.out.println("Executing request ::" + httpReq.getRequestLine() + " to " + "TargetUrl" + " via " + proxy);
             //CloseableHttpResponse responseObj = httpclient.execute(httpReq);
       
        HttpResponse responseObj = httpclient.execute(httpReq);
           
             //checking the response code
    /*if (responseObj.getStatusLine().getStatusCode() != 201) {
     throw new RuntimeException("Something Went wrong from server, error code : "
       + responseObj.getStatusLine().getStatusCode());
    }*/
             //read values from server
             iResCode= responseObj.getStatusLine().getStatusCode();
             System.out.println("----------------------------------------");
                System.out.println("ResponseCode--------->"+iResCode);
                sBfResCode.append(iResCode);
             sResFromGst= EntityUtils.toString(responseObj.getEntity());
             System.out.println("Response From GST::::"+sResFromGst);
             jObj = new JSONObject(sResFromGst);
  } catch (Exception excp) {
   gbmlpc.logger.error(excp.getMessage(), excp);
  }
  return jObj;
 }