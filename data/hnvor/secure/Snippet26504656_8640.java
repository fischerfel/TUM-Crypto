// set the connection timeout value (CURLOPT_TIMEOUT)
int timeout = 15;
RequestConfig config = RequestConfig.custom()
  .setSocketTimeout(timeout * 1000)
  .setConnectTimeout(timeout * 1000)
  .build();

//Create the request
HttpClient client = HttpClientBuilder
  .create()
  .setDefaultRequestConfig(config)
  //set your verifier (CURLOPT_SSL_VERIFYHOST)
  .setHostnameVerifier(new AbstractVerifier() {
      @Override
      public void verify(final String host, final String[] 
                          cns, final String[] subjectAlts) throws SSLException {
        verify(host, cns, subjectAlts, true);
      }
     }
   )
  .build();
HttpGet request = new HttpGet(url + theData);

//Get the response
HttpResponse response = client.execute(request);
String responseString = EntityUtils.toString(response.getEntity());
