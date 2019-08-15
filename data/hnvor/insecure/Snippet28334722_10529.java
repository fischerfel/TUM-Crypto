httpsConnection = (HttpsURLConnection) new URL(url).openConnection();

httpsConnection.setHostnameVerifier(new MyHostnameVerifier());

// another class file

public class MyHostnameVerifier extends HostnameVerifier() {
  @Override
  public boolean verify(String hostname, SSLSession session) {
   return true;
   }
 });
