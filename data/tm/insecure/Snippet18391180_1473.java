    protected String doInBackground(String... urls) {
    try {
     //Basic Auth setting
     Authenticator.setDefault(new Authenticator() {
         protected PasswordAuthentication getPasswordAuthentication()
         {
             final String username = "ID";
             final String password = "PASS";
          return new PasswordAuthentication(username, password.toCharArray());
         }
     });


     URL url = new URL(urls[0],urls[1],Integer.parseInt(urls[2]),urls[3]);
     HttpURLConnection urlCon;
     if (url.getProtocol().toLowerCase().equals("https")) {
      X509TrustManager easyTrustManager = new X509TrustManager() {
             public void checkClientTrusted(
                     X509Certificate[] chain,
                     String authType) throws CertificateException {
             }
             public void checkServerTrusted(
                     X509Certificate[] chain,
                     String authType) throws CertificateException {
             }
             public X509Certificate[] getAcceptedIssuers() {
                 return null;
             }
         };
         TrustManager[] trustAllCerts = new TrustManager[] {easyTrustManager};
         try {
             SSLContext sc = SSLContext.getInstance("TLS");
             sc.init(null, trustAllCerts, new java.security.SecureRandom());
             HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

         } catch (Exception e) {
                 e.printStackTrace();
         }
         HttpsURLConnection urlHttpsConnection = (HttpsURLConnection) url.openConnection();
         urlCon = urlHttpsConnection;
     } else {
      urlCon = (HttpURLConnection) url.openConnection();
     }
     urlCon.setRequestMethod("POST");
     urlCon.setDoOutput(true); 
     urlCon.setConnectTimeout(timeout);

     urlCon.setRequestProperty("Accept", "*/*");
     Log.v("HttpRequestManager","post value[" + urls[4] + "]");
     PrintStream ps = new PrintStream(urlCon.getOutputStream());
     ps.print(urls[4]);
     ps.close();

     InputStream in = urlCon.getInputStream();
     BufferedReader br = new BufferedReader(new InputStreamReader(in));
     String temp = null;
        StringBuffer bufStr = new StringBuffer();
     while((temp = br.readLine()) != null) {
         bufStr.append(temp);
     }
     return bufStr.toString();
    } catch (Exception e) {
     e.printStackTrace();
        return null;
    } finally {
    }
}
