@Override
public HttpsURLConnection getHttpsConnection() throws Exception {   
    HttpsURLConnection urlConnection = null;

    // Create a trust manager that does not validate certificate chains
    final TrustManager[] trustAllCerts = new TrustManager[] { 
            new X509TrustManager() {
                public void checkClientTrusted( final X509Certificate[] chain, final String authType ) {
                }

                public void checkServerTrusted( final X509Certificate[] chain, final String authType ) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

            }       
    } ;

    // Install the all-trusting trust manager
    final SSLContext sslContext = SSLContext.getInstance("SSL");

    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

    // Create all-trusting host name verifier
    HostnameVerifier allHostsValid = new HostnameVerifier() {

        public boolean verify(String hostname, SSLSession session) {              
            return true;          
        }           
    };

    // Install the all-trusting host verifier
    HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

    try {   
        URL myUrl = new URL(this.url);
        urlConnection = (HttpsURLConnection) myUrl.openConnection();    
    } catch (Exception e) { 
        throw new Exception("Error in getting connecting to url: " + url + " :: " + e.getMessage());

    }

    return urlConnection;

} //end of getHttpsConnection()

private void processHttpRequest(HttpsURLConnection connection, String method, Map<String, String> params) throws Exception {

    StringBuffer requestParams = new StringBuffer();

    if (params != null && params.size() > 0) {
           Iterator<String> paramIterator = params.keySet().iterator();
           while (paramIterator.hasNext()) {
               String key = paramIterator.next();
               String value = params.get(key);
               requestParams.append(URLEncoder.encode(key, "UTF-8"));
               requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
               requestParams.append("&");

           }
    }

    try {

        connection.setUseCaches(false);
        connection.setDoInput(true);

        if ("POST".equals(method)) {    
            // set request method to POST
            connection.setDoOutput(true);

        } else {
            // set request method to GET
            connection.setDoOutput(false);

        }

        String parameters = requestParams.toString();

        if ("POST".equals(method) && params != null && params.size() > 0) {

            OutputStream os = connection.getOutputStream();
            DataOutputStream wr = new DataOutputStream(os);
            wr.writeBytes(URLEncoder.encode(parameters, "UTF-8"));
            wr.writeBytes(parameters);
            wr.flush();
            wr.close();

            /**
             * 

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();  
            writer.close();
           */
        }

     // reads response, store line by line in an array of Strings
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

      List<String> response = new ArrayList<String>();

      String line = "";
      while ((line = reader.readLine()) != null) {
          response.add(line);
      }

      reader.close();

      String[] myResponse = (String[]) response.toArray(new String[0]);

      if (myResponse != null && myResponse.length > 0) {
          System.out.println("RESPONSE FROM: " + this.url);
          for (String myLine : response) {
              System.out.println(myLine);
          }
      }  
    } catch(Exception e) {  
        throw new Exception("Error in sending Post :: " + e.getMessage());

    }   
}
