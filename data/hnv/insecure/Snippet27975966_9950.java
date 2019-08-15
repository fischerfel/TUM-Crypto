  ....
  KeyStore trustStore=KeyStore.getInstance(KeyStore.getDefaultType());
  trustStore.load(null,null);
  SSLSocketFactory socketFactory=new InsecureSSLSocketFactory(trustStore);
  socketFactory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
  client.setSSLSocketFactory(socketFactory);
  client.post(null, "https://3sidedcube.com/api/jobs", se, "application/json", new         JsonHttpResponseHandler(){
  .....
