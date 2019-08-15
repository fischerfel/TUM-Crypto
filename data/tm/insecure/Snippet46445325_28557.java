public void call() throws IOException, ParserConfigurationException, SAXException, TransformerException
{
    OutputStreamWriter out = null;
      URL url = null;
      BufferedReader in = null;
      String username = "test";
    String password = "test";
    String xmlDoc = 
    String xmlDoc = "<?xml version=\"1.0\" encoding=\"iso-8859-1\"?> <document schema=\"mySchema.xsd\"><data><start-date>2012-03-10T00:00:00</start-date><end-date>2017-01-13T10:43:00</end-date></data></document>";
    try {

        // ----Avoid certificate Error Code Start--------
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkServerTrusted(X509Certificate[] chain,String authType) throws CertificateException {
            }

            public void checkClientTrusted(X509Certificate[] chain,
                             String authType) throws CertificateException {
            }
          } };

          SSLContext sc = null;
          try 
          {
              sc = SSLContext.getInstance("SSL");
          } 
          catch (NoSuchAlgorithmException e) {
              e.getMessage();
          }
          try 
          {
              sc.init(null, trustAllCerts, new java.security.SecureRandom());
          } 
          catch (KeyManagementException e) {
              e.getMessage();
          }
          HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory()); 

        url = new URL("https://myURL.com?username="+username+"&password="+password+"&xmlDocument="+xmlDoc+"");
        //URLEncoder.encode("https://service.bioguiden.se/mpaallexport.asmx/Export?username="+username+"&password="+password+"&xmlDocument="+xmlDoc+"","UTF-8");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setDoOutput(true);
      conn.connect();
      out = new OutputStreamWriter(conn.getOutputStream());

      StringBuffer data = new StringBuffer();
      out.write(data.toString());
      out.flush();

      // Read the response
      StringBuffer rsp = new StringBuffer();
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
         rsp.append(line);
      }
    } catch (MalformedURLException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
