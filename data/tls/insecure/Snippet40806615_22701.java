System.out.println("------------------------------------------- In         SendRequest ------------------------------------################");
SSLContext context = SSLContext.getInstance("TLS");
Certificate cert=getCertificate();
URL url = new URL("url");
URLConnection urlConnection = url.openConnection();
HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) urlConnection;
SSLSocketFactory sslSocketFactory = getFactory();
httpsUrlConnection.setSSLSocketFactory(sslSocketFactory);
DataOutputStream wr = new         DataOutputStream(httpsUrlConnection.getOutputStream());
System.out.println(wr.toString());
File req_xml = new File("request.xml");
//SOAPMessage req = TestCase.createSoapSubsribeRequest("SUBSCRIBE");
HttpPost post = new HttpPost("url");
post.setEntity(new InputStreamEntity(new FileInputStream(req_xml), req_xml.length()));
post.setHeader("Content-type", "text/xml; charset=UTF-8");
//post.setHeader("SOAPAction", "");
HttpClient client = new DefaultHttpClient();
HttpResponse response = client.execute(post);

LOG.info("************************************************************RESPONSE****************"+response.getStatusLine());
// SOAP response(xml) get        String res_xml = EntityUtils.toString(response.getEntity());
    LOG.info("Response"+res_xml);
}
private SSLSocketFactory getFactory( )  {
    try{ 
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        System.out.println("------------------------------------------- In getFactory ------------------------------------################");
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
        //InputStream keyInput = new FileInputStream(pKeyFile);
        String password = "obsmesh";
                    char[] passwd = password.toCharArray(example.jks");
        keystore.load(is, passwd);
        // keyInput.close();
        keyManagerFactory.init(keystore, password.toCharArray());
        System.out.println("------------------------------------------- In jsdkl ------------------------------------################");
        SSLContext context = SSLContext.getInstance("TLS");
        TrustManager[] trust = null;
        context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
        return context.getSocketFactory();
}catch(Exception e){
    System.out.println(e);
}
return null;
