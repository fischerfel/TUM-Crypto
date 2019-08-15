CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));

getRequest("https://online.firstdata.de/esp/concardis/",null,false);

String postParams = "?j_username="+URLEncoder.encode(userName, "UTF-8")+"&j_password="+URLEncoder.encode(password, "UTF-8");


sendPost("https://online.firstdata.de/login/j_security_check",postParams);

private void sendPost(String url,String postParams) throws Exception {


    // Create a trust manager that does not validate certificate chains
     TrustManager[] trustAllCerts = new TrustManager[] { 
         new X509TrustManager() {     
             public java.security.cert.X509Certificate[] getAcceptedIssuers() { 
                 return new X509Certificate[0];
             } 
             public void checkClientTrusted( 
                 java.security.cert.X509Certificate[] certs, String authType) {
                 } 
             public void checkServerTrusted( 
                 java.security.cert.X509Certificate[] certs, String authType) {
             }
         } 
     }; 

     // Install the all-trusting trust manager
     try {
         SSLContext sc = SSLContext.getInstance("SSL"); 
         sc.init(null, trustAllCerts, new java.security.SecureRandom()); 
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
     } catch (GeneralSecurityException e) {
     } 
     // Now you can access an https URL without having the certificate in the truststore

        HttpsURLConnection conn;
        URL obj = new URL(url);
        conn = (HttpsURLConnection) obj.openConnection();

        byte[] postData       = postParams.getBytes( StandardCharsets.UTF_8 );
        // Acts like a browser
        conn.setRequestMethod("POST");

        conn.setRequestProperty("Accept",                   "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        conn.setRequestProperty("Accept-Encoding","gzip,deflate");
        conn.setRequestProperty("Accept-Language", "en-GB,en;q=0.8,en-US;q=0.6,da;q=0.4");
        conn.setRequestProperty("Cache-Control", "max-age=0");
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Length", Integer.toString(postData.length ));
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");//text/html;charset=ISO-8859-1
        //conn.setRequestProperty( "charset", "utf-8");
        for (String cookie : this.cookies) {
            conn.addRequestProperty("Cookie", cookie);
        }
        conn.addRequestProperty("Cookie","NSC_POMJOF-GE=ffffffff09425a7045525d5f4f58455e445a4a42122b");
        conn.setRequestProperty("Host","online.firstdata.de");
        conn.setRequestProperty("Origin","https://online.firstdata.de");
        conn.setRequestProperty("Referer", "https://online.firstdata.de/login/postlogin/UserDispatcher");
        conn.setRequestProperty("User-Agent", USER_AGENT);  
        conn.setUseCaches(false);
        conn.setDoOutput(true);
        HttpsURLConnection.setFollowRedirects(false);
        conn.setInstanceFollowRedirects(false);
        conn.connect();

        // Send post request

        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();

        int responseCode = conn.getResponseCode();

            // Get the response cookies
            if(conn.getHeaderFields().containsKey("Set-Cookie")){

                for(String cookie : conn.getHeaderFields().get("Set-Cookie")){
                    this.cookies.add(cookie);
                }
            }
            String location = "";
            if(conn.getHeaderFields().containsKey("Location")){

                location = conn.getHeaderFields().get("Location").get(0);
            }
            conn.disconnect();
}




private String getRequest(String url,String referer,boolean isPostParams) throws Exception {
    HttpsURLConnection conn;
    URL obj = new URL(url);
    conn = (HttpsURLConnection) obj.openConnection();

    conn.setRequestMethod("GET");

    conn.setUseCaches(false);

    // act like a browser
    conn.setRequestProperty("Accept",
            "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

    conn.setRequestProperty("Accept-Language", "en-GB,en;q=0.8,en-US;q=0.6,da;q=0.4");
    conn.setRequestProperty("Cache-Control", "max-age=0");
    conn.setRequestProperty("Connection", "keep-alive");
    conn.setRequestProperty("Host","online.firstdata.de");
    conn.addRequestProperty("Cookie","NSC_POMJOF-GE=ffffffff09425a7045525d5f4f58455e445a4a42122b");
    if(referer!=null)
        conn.setRequestProperty("Referer", referer);
    conn.setRequestProperty("User-Agent", USER_AGENT);
    conn.setDoOutput(true);
    conn.setDoInput(true);
    HttpsURLConnection.setFollowRedirects(false);
    conn.setInstanceFollowRedirects(false);
    if (cookies != null) {
        for (String cookie : this.cookies) {
            conn.addRequestProperty("Cookie", cookie);
        }
    }
    conn.connect();

    if(isPostParams){
        DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
        wr.writeBytes(postParams);
        wr.flush();
        wr.close();
    }


    int responseCode = conn.getResponseCode();

    BufferedReader in =
            new BufferedReader(new InputStreamReader(conn.getInputStream()));
    String inputLine;
    StringBuffer response = new StringBuffer();

    while ((inputLine = in.readLine()) != null) {
        response.append(inputLine+"\n");
    }
    in.close();

    // Get the response cookies
    if(conn.getHeaderFields().containsKey("Set-Cookie")){

        for(String cookie : conn.getHeaderFields().get("Set-Cookie")){

            this.cookies.add(cookie);
        }
    }

    String location = "";
    if(conn.getHeaderFields().containsKey("Location")){

        location = conn.getHeaderFields().get("Location").get(0);
    }
    conn.disconnect();

    return location;
  }
