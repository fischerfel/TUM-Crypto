HttpParams myParams = new BasicHttpParams();
HttpProtocolParams.setVersion(myParams, HttpVersion.HTTP_1_1);
HttpProtocolParams.setContentCharset(myParams, "utf-8");
myParams.setBooleanParameter("http.protocol.expect-continue", false);


HttpConnectionParams.setConnectionTimeout(myParams, 100000); 
HttpConnectionParams.setSoTimeout(myParams, 100000);

//
KeyStore trusted;
try {  

        trusted = KeyStore.getInstance("pkcs12");
        trusted.load(null, "".toCharArray());
        SSLSocketFactory sslf = new SSLSocketFactory(trusted);
  sslf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER); 

  SchemeRegistry schemeRegistry = new SchemeRegistry(); 
  schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

  schemeRegistry.register(new Scheme("https", sslf, 443));    
  ThreadSafeClientConnManager manager = new ThreadSafeClientConnManager(myParams, schemeRegistry);


  httpClient = new DefaultHttpClient(manager, myParams); 
  } catch (KeyStoreException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (NoSuchAlgorithmException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (CertificateException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (IOException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (KeyManagementException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  } catch (UnrecoverableKeyException e) {
   // TODO Auto-generated catch block
   e.printStackTrace();
  }         
    //

    localContext = new BasicHttpContext();  

    httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.RFC_2109); 

    httpPost = new HttpPost("https://...."); 
    response = null; 

    StringEntity tmp = null;         

    httpPost.setHeader("SOAPAction", "SoapActionURL"); 

    if (contentType != null) { 
        httpPost.setHeader("Content-Type", contentType); 
    } else { 
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded"); 
    } 

    try { 
        tmp = new StringEntity(data,"UTF-8"); 
    } catch (UnsupportedEncodingException e) { 
        Log.e("Your App Name Here", "HttpUtils : UnsupportedEncodingException : "+e); 
    } 

    httpPost.setEntity(tmp); 

    try { 

        response = httpClient.execute(httpPost,localContext); 

        if (response != null) { 
            ret = EntityUtils.toString(response.getEntity()); 
        } 
    } catch (Exception e) { 
        Log.e("Your App Name Here", "HttpUtils: " + e); 
    }  
