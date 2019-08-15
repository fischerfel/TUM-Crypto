/**
 * This utility function will upload the file to the Url
 * * @param filePath - absolute path of the file to be uploaded
 * @param postUrl  - Remote Url where the file need to be posted
 * @param contentType - content-type of the uploaded file
 * @throws Exception
 */
public static void postFile(String filePath, String postUrl,
        String pictureTitleStr, String pseudoTextStr)
        throws Exception {

    String url = postUrl;
    HttpURLConnection conn = null;
    final String CrLf = "\r\n";
    JSONObject json = new JSONObject();
    int bytesRead = 0;


    String lineEnd = "\r\n";
    String twoHyphens = "--";
    String boundary = "xxxxxxxx";
    String EndBoundary = "";
    int maxBufferSize = 1 * 1024 * 1024;

    HttpResponse response = null;

  // Having HttpClient to respond to both HTTP and HTTPS url connection by accepting the urls along with keystore / trust certificates 

    try {
        KeyStore trustStore = KeyStore.getInstance(KeyStore
                .getDefaultType());
        trustStore.load(null, null);

        SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
        sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpParams params = new BasicHttpParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
        HttpProtocolParams.setUserAgent(params, "YourAppName/1.1");
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
        HttpConnectionParams.setSoTimeout(params, 20 * 1000);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        HttpClientParams.setRedirecting(params, false);

        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory
                .getSocketFactory(), 80));
        registry.register(new Scheme("https", sf, 443));

        ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                params, registry);

        mHttpClient = new DefaultHttpClient(ccm, params);



    } catch (Exception e) {

    }

    String base64EncodedCredentials = Base64.encodeToString((userName + ":" + password).getBytes("US-ASCII"),
            Base64.DEFAULT);
    System.out.println("Encoded Credit " + base64EncodedCredentials);

            json.put("pseudo", pseudoTextStr);
            json.put("title", pictureTitleStr);

           String jsonStr = json.toString(); 
 //   System.out.println("JSON VALUE  " + jsonStr);

    URL url2 = new URL(postUrl);



    Bitmap bm = BitmapFactory.decodeFile(filePath);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.JPEG, 25, baos); // bm is the bitmap object
    byte[] b = baos.toByteArray();

    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);


    String str = twoHyphens + boundary + lineEnd;
    String str2 = "Content-Disposition: form-data; name=\"jsonFile\"";
    String str3 = "Content-Type: application/json";
    String str4 = "Content-Disposition: form-data; name=\"imgName\"";
    String str5 = "Content-Type: image/jpeg";
    String str6 = twoHyphens + boundary + twoHyphens;



    String StrTotal = str + str2 + "\r\n" + str3 + "\r\n" +"\r\n" + jsonStr + "\r\n" + str
            + str4 + "\r\n" + str5 + "\r\n"+"\r\n"+ encodedImage + "\r\n" + str6;

    //System.out.print("Multipart request string is "+StrTotal);

 HttpPost post = new HttpPost(postUrl);


post.addHeader(BasicScheme.authenticate(new UsernamePasswordCredentials(
                userName, password), "UTF-8", false));
post.addHeader("Content-Type","multipart/form-data;boundary="+boundary);
// System.out.println("Sending Post proxy request: " + post);

 StringEntity se = new StringEntity(StrTotal);
 se.setContentEncoding("UTF-8");
 post.setEntity(se);
 response = mHttpClient.execute(post);

/* Checking response */

statusCode = response.getStatusLine().getStatusCode();
System.out.println("Http Execute finish " + statusCode);

HttpEntity entity = response.getEntity();
String getResponseText = entity.toString(); // EntityUtils.toString(entity);
System.out.println(" Post Response Text from Server : "
        + getResponseText);



}
