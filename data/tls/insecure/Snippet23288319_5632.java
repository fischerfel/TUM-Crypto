public class Sender {

      public void send(String endpoint) {
           URL url = new URL(endpoint);
           HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) url.openConnection();
           httpsUrlConnection.setRequestMethod(new String("POST"));
           httpsUrlConnection.setDoOutput(true);
           httpsUrlConnection.setRequestProperty("Content-Type", "application/xml");
           SSLContext sc = SSLContext.getInstance("SSL");
           sc.init(null, getTrustManager(), new SecureRandom());
           httpsUrlConnection.setSSLSocketFactory(sc.getSocketFactory());
           httpsUrlConnection.connect();
           OutputStreamWriter outputStreamWriter = new OutputStreamWriter(httpsUrlConnection.getOutputStream());
           outputStreamWriter.write(""); // writes XML data
           outputStreamWriter.flush();
           outputStreamWriter.close();
           InputStreamReader inputStreamReader = new InputStreamReader(httpsUrlConnection.getInputStream());
           char i = (char) inputStreamReader.read();
           System.out.println(i);
      }

      public TrustManager[] getTrustManager() {
   TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
       public void checkClientTrusted(X509Certificate[] xcs, String string) throws CertificateException {

       }

       public void checkServerTrusted(X509Certificate[] xcs, String string) throws CertificateException {
           System.out.println("AuthType : " + " " + string);
           for(int i = 0; i < xcs.length; i++) {
               System.out.println("\t" + xcs[i].getIssuerX500Principal().getName());
               System.out.println("\t" + xcs[i].getIssuerDN().getName());
           }
       }

       public X509Certificate[] getAcceptedIssuers() {
           return null;
       }
   }};
   return trustAllCerts;
