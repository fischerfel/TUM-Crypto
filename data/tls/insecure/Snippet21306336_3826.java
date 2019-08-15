public class HttpRequestController {

private final static String TAG = "HttpRequestController";

private static HttpRequestController instance;

public enum Method {
    PUT, POST, DELETE, GET
}

private HttpRequestController() {

}

public static HttpRequestController getInstance() {
    if (instance == null)
        instance = new HttpRequestController();

    return instance;
}

public String doRequest(String url, HashMap<Object, Object> data,
        Method method, String token) throws Exception {

    InputStream certificateInputStream = null;
    if (MyApplication.PRODUCTION) {
        certificateInputStream = MyApplication.context
                .getResources().openRawResource(R.raw.production_cert);
        LogUtils.log("using production SSL certificate");
    } else {
        certificateInputStream = MyApplication.context
                .getResources().openRawResource(R.raw.staging_cert);
        LogUtils.log("using staging SSL certificate");
    }

    KeyStore trustStore = KeyStore.getInstance("BKS");
    try{
    trustStore.load(certificateInputStream,
            "re3d6Exe5HBsdskad8efj8CxZwv".toCharArray());
    } finally {
        certificateInputStream.close();
    }


    TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
    tmf.init(trustStore);
    LogUtils.log("SSL: did init TrustManagerFactory with trust keyStore");
    SSLContext context = SSLContext.getInstance("TLS");
    context.init(null, tmf.getTrustManagers(), null);
    LogUtils.log("SSL: did init context with trust keyStore");  


    URL request = new URL(url);
    HttpsURLConnection urlConnection = (HttpsURLConnection) request
            .openConnection();

    LogUtils.log("SSL: did open HttpsURLConnection");   

    urlConnection.setHostnameVerifier(new StrictHostnameVerifier());
    urlConnection.setSSLSocketFactory(context.getSocketFactory());
    urlConnection.setConnectTimeout(15000);
    LogUtils.log("SSL: did set Factory and Timeout.");

    if (method != Method.GET){
        urlConnection.setDoOutput(true);
    }
        urlConnection.setDoInput(true);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");

    LogUtils.log("SSL: urlConnection did set request properties.");

    if (token != null) {
        urlConnection.setRequestProperty("Authorization", "Token " + token);
    }
        urlConnection.setRequestMethod(method.toString());
        urlConnection.connect();

        LogUtils.log("SSL: urlConnection did connect.");

    if (method != Method.GET) {
        ObjectMapper mapper = new ObjectMapper();
        String jsonValue = mapper.writeValueAsString(data);
        OutputStream os = urlConnection.getOutputStream();
        os.write(jsonValue.getBytes());
        os.flush();
        LogUtils.log(TAG, "Params: " + jsonValue);
    }

    LogUtils.log(TAG, method.toString() + ": " + url);

    InputStream in = null;
    if (urlConnection.getResponseCode() == 200) {
        in = urlConnection.getInputStream();
    } else {
        in = urlConnection.getErrorStream();
    }
    String response = convertStreamToString(in);

    LogUtils.log(TAG, "Got response : " + url);
    LogUtils.log(TAG, "Response : " + response);

    return response;
}

public String convertStreamToString(InputStream inputStream) {
    BufferedReader buffReader = new BufferedReader(new InputStreamReader(
            inputStream));
    StringBuilder stringBuilder = new StringBuilder();

    String line = null;
    try {
        while ((line = buffReader.readLine()) != null) {
            stringBuilder.append(line + "\n");
        }
    } catch (IOException e) {
        e.printStackTrace();
    } finally {
        try {
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    return stringBuilder.toString();
}

public HttpClient retrieveHttpClient() {
    return new MyHttpClient(MyApplication.context);
}
