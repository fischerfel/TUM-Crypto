import javax.net.ssl.HttpsURLConnection;
   import javax.net.ssl.SSLContext;
   import javax.net.ssl.TrustManager;
   import javax.net.ssl.X509TrustManager;
   import java.security.cert.X509Certificate;  
    import java.io.*;
   import java.net.*;
    import javax.net.ssl.*;
    import java.security.*;


   public class CssAttack {

         public static void test() throws NoSuchAlgorithmException, KeyManagementException {
         TrustManager trm = new X509TrustManager() {
               public X509Certificate[] getAcceptedIssuers() {
                  return null;
               }

               public void checkClientTrusted(X509Certificate[] certs, String authType) {

               }

               public void checkServerTrusted(X509Certificate[] certs, String authType) {
               }
            };

         SSLContext sc = SSLContext.getInstance("SSL");
         sc.init(null, new TrustManager[] { trm }, null);
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      }


      public static void main(String[] args) throws IOException {
            try{
                test();
         }
            catch(Exception e){

            }


         String httpsURL = "website";

         String query = "email="+URLEncoder.encode("blah@blah.com","UTF-8"); 
         query += "&";
         query += "keyword="+URLEncoder.encode("PRETZEL","UTF-8");
            query += "&";
            query += "login="+URLEncoder.encode("Login","UTF-8");

         URL myurl = new URL(httpsURL);
         HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
         con.setRequestMethod("POST");

         con.setRequestProperty("Content-length", String.valueOf(query.length())); 
         con.setRequestProperty("Content-Type","application/x-www-form-urlencoded"); 
         con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)"); 
         con.setDoOutput(true); 
         con.setDoInput(true); 

         DataOutputStream output = new DataOutputStream(con.getOutputStream());  


         output.writeBytes(query);

         output.close();

         DataInputStream input = new DataInputStream( con.getInputStream() );  

         System.out.println("Resp Code:"+con .getResponseCode()); 
         System.out.println("Resp Message:"+ con .getResponseMessage()); 
      }
    }
