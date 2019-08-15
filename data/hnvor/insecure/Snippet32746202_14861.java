    import java.io.BufferedReader;
        import java.io.DataOutputStream;
        import java.io.IOException;
        import java.io.InputStreamReader;
        import java.io.UnsupportedEncodingException;
        import java.net.MalformedURLException;
        import java.net.URL;
        import java.security.KeyManagementException;
        import java.security.NoSuchAlgorithmException;
        import javax.net.ssl.HostnameVerifier;
        import javax.net.ssl.HttpsURLConnection;
        import javax.net.ssl.SSLSession;
        import org.apache.commons.codec.binary.Base64;

        public class VormetricClientToken {



        public static void main(String[] args) throws NoSuchAlgorithmException, KeyManagementException, UnsupportedEncodingException {
              new VormetricClientToken().DoIt();
           }//main

           private void DoIt() throws NoSuchAlgorithmException, KeyManagementException, UnsupportedEncodingException{
              String credential = Base64.encodeBase64String("myuser:mypass".getBytes("UTF-8"));
              try{
       String https_url = "https://192.168.1.13/vts/rest/v1.0/tokenize/";
       URL myurl = new URL(https_url);
       HttpsURLConnection con = HttpsURLConnection)myurl.openConnection();
       con.setHostnameVerifier(new HostnameVerifier(){      
          @Override
          public boolean verify(String hostname, SSLSession session){
             return true;
          }
       });
       String ccNum = "9876-5432-1098-7654";
       String jStr = "{\"tokengroup\" : \"pruebas\" , \"data\" :    \""+ccNum+"\", \"format\" : \"random-luhn\"}";
       con.setRequestProperty("Content-length", String.valueOf(jStr.length()));
       con.setRequestProperty("Content-Type","application/json; charset=UTF-8");
       byte[] ptext = jStr.getBytes("UTF-8");
       con.setRequestProperty("Authorization","Basic "+credential);
       con.setRequestMethod("POST");
       con.setDoOutput(true);
       try (DataOutputStream output = new DataOutputStream(con.getOutputStream())) {
          output.write(jStr.getBytes());
       }
       int responseCode = con.getResponseCode();
       System.out.println("Response Code : " + responseCode);
       BufferedReader rd= new BufferedReader(new    InputStreamReader(con.getInputStream()));
       String line = "";      
       String strResponse = "";

       while((line = rd.readLine()) != null) {
          strResponse=strResponse+line;
       }
       con.disconnect();
       System.out.println("POST response: "+strResponse);
    }catch(MalformedURLException e) {
       System.out.println("error vormetric token client malformed:"+e);
    }catch(IOException e1){
       System.out.println("error vormetric token client ioexception:"+e1);
       e1.printStackTrace();
    }//catch
 }//doit
}//vormetricclienttoken
