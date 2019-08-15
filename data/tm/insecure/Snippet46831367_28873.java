     SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, new TrustManager[] {new X509TrustManager(){
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType){

                }
                public void checkServerTrusted(X509Certificate[] certs, String authType){
                }
            }}, new SecureRandom());

    CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(new AuthScope("proxy-host", port), new NTCredentials("user","password",null, "domain"));

    SSLConnectionSocketFactory connectionFactory = new SSLConnectionSocketFactory(sslContext);

    CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(connectionFactory).setDefaultCredentialsProvider(credentialsProvider).setSSLContext(sslContext).build();

    HttpHost target = new HttpHost("targethost", -1, "https");
    HttpHost proxy= new HttpHost("proxyhost", proxy-port, "http");


    RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
    HttpGet request = new HttpGet("/sample.pdf");
    request.setConfig(config);

    CloseableHttpResponse = null;
    try{
      response = httpClient.execute(target, request);
      InputStream is = response.getEntity().getContent().read());
      FileOutputStream fos = new FileOutputStream(new File(filePath));
      int inByte;
      while((inByte = is.read()) != -1){
         fos.write(inByte);
      }
      is.close();
      fos.close();
   } catch (IOException e) {
      e.printStackTrace();
   }
