public class Example {
   public static void main(String[] args) throws Exception {
     TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

         @Override
         public java.security.cert.X509Certificate[] getAcceptedIssuers() {
             return null;
         }

         @Override
         public void checkClientTrusted(X509Certificate[] certs, String authType) {
         }

         @Override
         public void checkServerTrusted(X509Certificate[] certs, String authType) {
         }
     }
     };

     SSLContext sc = SSLContext.getInstance("SSL");
     sc.init(null, trustAllCerts, new java.security.SecureRandom());
     HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

     HostnameVerifier allHostsValid = new HostnameVerifier() {

         @Override
         public boolean verify(String hostname, SSLSession session) {
             return true;
         }

     };

     HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
     String url = "https://icpcarchive.ecs.baylor.edu/";
     URL obj = new URL(url);
     HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
     con.setRequestMethod("POST");
     con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

     String username = "MyUsername";
     String password = "MyPassword";
     String urlParameters = "username=" + username + "&passwd=" + password
             + "&Submit=Login";

     con.setDoOutput(true);

     try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) {
         wr.writeBytes(urlParameters);
         wr.flush();
     }

     BufferedReader in = new BufferedReader(
             new InputStreamReader(con.getInputStream()));
     String inputLine;
     StringBuilder response = new StringBuilder();

     while ((inputLine = in.readLine()) != null) {
         response.append(inputLine);
     }
     in.close();
     System.out.println(response.toString());

 }

}
