public class GetConnectionMethod_JavaNet {

private String url;
private int STATUS_CODE = -1;
private String requestData;
private int isContentChanged = Constants.NO_NEW_CONTENT_AVAIL;  //304 means no change in content
private HttpsURLConnection urlConnection;
//private DefaultHttpClient httpclient;
private InputStream inputStream ;
public SSLContext sslContext;
PreferenceManagerUtil pmUtil = new PreferenceManagerUtil();
public GetConnectionMethod_JavaNet(String url, String requestData ) {

 this.url = url;
 this.requestData = requestData;

}

/**
 * setting the headers
 */
private void setHeaders () {

 String FBToken = pmUtil.getPreferenceValue(Constants.FBTOKEN, "FACEBOOKTOKEN");

 urlConnection.setRequestProperty("Connection","Keep-Alive");
 urlConnection.addRequestProperty(Constants.APIVERSION, Constants.APIVERSIONVALUE);
 urlConnection.addRequestProperty(Constants.PLATFORM, Constants.PLATFORMVALUE);
 urlConnection.addRequestProperty(Constants.FACEBOOK_TOKEN,FBToken);
 urlConnection.addRequestProperty(Constants.CONTENT_TYPE, Constants.APPLICATIONJSON );
}

/**
 * Trust every server - dont check for any certificate
 */
private static void trustAllHosts() {

 KeyStore trustStore = null;
 try {
  trustStore = KeyStore.getInstance("BKS");
 } catch (KeyStoreException e2) {
  // TODO Auto-generated catch block
  e2.printStackTrace();
 }
 try {
  trustStore.load(getApplicationContext().getResources().openRawResource(R.raw.my_certificate),
         "mypassword".toCharArray());
 } catch (NoSuchAlgorithmException e1) {
  // TODO Auto-generated catch block
  e1.printStackTrace();
 } catch (CertificateException e1) {
  // TODO Auto-generated catch block
  e1.printStackTrace();
 } catch (NotFoundException e1) {
  // TODO Auto-generated catch block
  e1.printStackTrace();
 } catch (IOException e1) {
  // TODO Auto-generated catch block
  e1.printStackTrace();
 }
        TrustManagerFactory trustMgr = null;
   try {
    trustMgr = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
   } catch (NoSuchAlgorithmException e1) {
    // TODO Auto-generated catch block
    e1.printStackTrace();
   }
        try {
    trustMgr.init(trustStore);
   } catch (KeyStoreException e1) {
    // TODO Auto-generated catch block
    e1.printStackTrace();
   }


               try {
                SSLContext sc = SSLContext.getInstance("TLS");

                sc.init(null, trustMgr.getTrustManagers(), new java.security.SecureRandom());
                HttpsURLConnection
                                .setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
                e.printStackTrace();
        }
}







 public Object send ( int returnType ) {

 try {
  URL url = new URL(this.url);

   System.setProperty("http.keepAlive", "false");
   trustAllHosts();
  if ( urlConnection == null ) {
   urlConnection = (HttpsURLConnection) url.openConnection();//Without Proxy
  }

urlConnection.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
  setHeaders();

  STATUS_CODE = urlConnection.getResponseCode();


  if(STATUS_CODE == 201  || STATUS_CODE == 200 ){
   inputStream = urlConnection.getInputStream();
  }else{

   inputStream = urlConnection.getErrorStream();
  }  

  switch ( returnType ) {

  case Constants.TYPE_BYTE :   return null;
  case Constants.TYPE_STREAM:  return null;
  case Constants.TYPE_STRINGBUFFER :  

   if ( inputStream != null) {
    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream),8 * 1024);
    StringBuilder sb = new StringBuilder();
    String line = null;
    while ((line = reader.readLine()) != null) {
     sb.append(line + "\n");
    }
    return new StringBuffer ( sb.toString() );
   }
  }

  return null;
 } catch ( UnknownHostException e ) {
  e.printStackTrace();
  STATUS_CODE = 001;

 } catch (IOException e) {
  e.printStackTrace();
  return new StringBuffer("UNAUTHORIZED");  

 } catch ( Exception e ) {
  e.printStackTrace();
 }catch (Error e) {
  e.printStackTrace();
 }

 finally {
  try{
   // release the memory
   if(urlConnection!=null)
    urlConnection.disconnect();
  }catch (Exception e) {
  }
 }

 return null;
}

public int getStateCode(){

 return STATUS_CODE;
}

public int isNewContentAvaialable(){

 return isContentChanged;
}



}
