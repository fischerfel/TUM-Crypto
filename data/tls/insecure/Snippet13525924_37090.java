public class HttpsClient {

private final static String TAG = "HttpsClient";

private final static String TOKEN_HEADER_KEY = "Token";

private final String urlString;

private SSLContext sslContext;

// application-specific HTTP header
private String TokenHeaderValue = null;



public HttpsClient(String host, String path) {
    // this.sslContext will be init'ed in open()
    this.urlString = "https://" + host + ":443/" + path;
}


public boolean open() {
    try {
        this.sslContext = SSLContext.getInstance("TLS");
        this.sslContext.init(null, null, new java.security.SecureRandom());
        return true;
    } catch (NoSuchAlgorithmException e) {
        Logger.e(TAG, "NoSuchAlgorithmException:");
    } catch (KeyManagementException e) {
        Logger.e(TAG, "KeyManagementException:");
    }

    return false;
}


public byte[] send(byte[] req) {

    Logger.d(TAG, "sending " + Utils.byteArrayToString(req) + " to " + this.urlString);

    URL url;
    try {
        url = new URL(this.urlString);
    } catch (MalformedURLException e) {
        Logger.e(TAG, "MalformedURLException:");
        return null;
    }
    HttpsURLConnection connection;
    try {
        connection = (HttpsURLConnection) url.openConnection();
    } catch (IOException e) {
        Logger.e(TAG, "send IOException 1 " + ((null == e.getMessage()) ? e.getMessage() : ""));
        e.printStackTrace();
        return null;
    }

    connection.setDoInput(true);
    connection.setDoOutput(true);
    connection.setRequestProperty("Connection", "close");
    try {
        connection.setRequestMethod("POST");

    } catch (ProtocolException ignored) { }
    connection.setSSLSocketFactory(this.sslContext.getSocketFactory());
    connection.setReadTimeout(3000);



    if ( this.TokenHeaderValue != null )
        connection.setRequestProperty(TOKEN_HEADER_KEY, this.TokenHeaderValue);



    {
        final Map<String, List<String>> requestProps = connection.getRequestProperties();
        Logger.d(TAG, requestProps.size() + " Request header(s):");
        for (Map.Entry<String, List<String>> entry : requestProps.entrySet())
            for (String value : entry.getValue())
                Logger.d(TAG, " " + entry.getKey() + ": <" + value + ">");
    }

    try {
        // open up the output stream of the connection 
        DataOutputStream output = new DataOutputStream(connection.getOutputStream()); 

        // write out the data 
        output.write(req, 0, req.length);
        output.flush();


        Logger.i(TAG, "Response Code: " + connection.getResponseCode());
        Logger.i(TAG, "Response Message: " + connection.getResponseMessage()); 
    } catch (SocketTimeoutException e) {
        Logger.e(TAG, "SocketTimeoutException:" + ((null == e.getMessage()) ? e.getMessage() : ""));
        return null;
    } catch (IOException e) { // FAILS HERE !!!!!!!
        Logger.e(TAG, "send IOException 2 " + ((null == e.getMessage()) ? e.getMessage() : ""));
        return null;
    }


    final Map<String, List<String>> responseHeaderFields = connection.getHeaderFields();
    Logger.d(TAG, responseHeaderFields.size() + " Response header(s):");
    for (Map.Entry<String, List<String>> entry : responseHeaderFields.entrySet()) {
        final String key = entry.getKey();
        if ( (null != key) && key.equals(TOKEN_HEADER_KEY) )
            this.TokenHeaderValue = entry.getValue().get(0);
        for (String value : entry.getValue())
            Logger.d(TAG, " " + key + ": <" + value + ">");
    }


    // read response
    ArrayList<Byte> response = new ArrayList<Byte>();

    try {
        DataInputStream input = new DataInputStream(connection.getInputStream()); 

        // read in each character until end-of-stream is detected
        for( int c = input.read(); c != -1; c = input.read() ) {
            response.add((byte) c);
        }
        Logger.w(TAG, "Https connection is " + connection);
        connection.disconnect();
        Logger.w(TAG, "Https connection is " + connection);
        input.close();

    } catch (IOException e) {
        Logger.e(TAG, "send IOException 3 " + ((null == e.getMessage()) ? e.getMessage() : ""));
        return null;
    }

    if ( 0 == response.size() ) {
        Logger.w(TAG, "response is null");

        return null;
    }

    // else

    byte[] result = new byte[response.size()];
    for (int i = 0; i < result.length; i++)
        result[i] = response.get(i).byteValue();

    Logger.i(TAG, "Response payload: " + Utils.byteArrayToString(result));



    return result;          
}
}
