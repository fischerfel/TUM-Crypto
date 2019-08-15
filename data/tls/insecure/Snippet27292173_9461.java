class VerifyTask extends AsyncTask<String, String, String> {
    String Code;

    VerifyTask(String Code) { 
        this.Code = Code;
    }

    @Override
    protected String doInBackground(String... params) {
        String response = null;
        BufferedReader reader = null;
            try {
                final TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(
                               java.security.cert.X509Certificate[] chain,
                               String authType) throws CertificateException {
                          // TODO Auto-generated method stub

                   }

                    @Override
                    public void checkServerTrusted(
                               java.security.cert.X509Certificate[] chain,
                               String authType) throws CertificateException {
                          // TODO Auto-generated method stub

                   }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                          // TODO Auto-generated method stub
                          return null ;
                   }

                } };
                   // Install the all-trusting trust manager
                   final SSLContext sslContext = SSLContext.getInstance("SSL");
                  sslContext.init( null, trustAllCerts,
                               new java.security.SecureRandom());
                   // Create an ssl socket factory with our all-trusting manager
                   final javax.net.ssl.SSLSocketFactory sslSocketFactory = sslContext
                              .getSocketFactory();


                   final URLConnection connection = (HttpsURLConnection) new URL("https://myserver.com/api/v1/verify?code=" + Code).openConnection();
                   ((HttpsURLConnection) connection).setSSLSocketFactory(sslSocketFactory);
                   ((HttpsURLConnection) connection).setRequestMethod("GET");
                   ((HttpsURLConnection) connection).setRequestProperty("Authorization", token);
                   ((HttpsURLConnection) connection).setRequestProperty("Connection", "Keep-Alive");
                   ((HttpsURLConnection) connection).setRequestProperty("Accept", "application/json, */*");                    
                   ((HttpsURLConnection) connection).setUseCaches(false);

                   try{

                       reader = new BufferedReader(new InputStreamReader(
                              ((HttpsURLConnection) connection).getInputStream()));
                       StringBuffer sb = new StringBuffer("");
                       String line = "";
                       while ((line = reader.readLine()) != null) {
                         sb.append(line).append("\n");
                       }
                        //view data
                        response = sb.toString();
                        System.out.println("RESP=" + response);
                   } catch (IOException e) {
                        int responsecode = ((HttpURLConnection) connection).getResponseCode();
                        System.out.println("SERVERERR=" + String.valueOf(responsecode) + ": "+((HttpURLConnection) connection).getResponseMessage());
                   }

            } catch (Exception e) {
                    System.out.println("Exp=" + e);
            }
            return null;
    }

    @Override
    protected void onPostExecute(String result) {

        //dialog.dismiss();
        super.onPostExecute(result);

    }

    @Override
    protected void onPreExecute() {

        //dialog.setMessage("Loading...");
        //dialog.setIndeterminate(true);
        //dialog.setCancelable(true);
        //dialog.show();
        super.onPreExecute();

    }
}
