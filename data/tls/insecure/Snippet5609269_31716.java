//my helper class
public class SmartDBHelper {

    private static Context tThis;
    private static SmartDBHelper sDBHObject;
    private static String macAddress;
    private static String ipAddress;
    private static HttpsURLConnection https;

    /* constructor, private prevents any other class from instantiating */
    private SmartDBHelper() {

    }

    public static synchronized SmartDBHelper getSDBHObject() {
        if(sDBHObject == null) {
            sDBHObject = new SmartDBHelper();
        }
        return sDBHObject;
    }

    public Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException();
    }

    public static synchronized void setSmartContext(SmartApp smartApp) {
        tThis = (Context) smartApp;
    }

    private static synchronized void setMACIPAddress() {
        WifiManager wifiMan = (WifiManager) tThis.getSystemService (tThis.WIFI_SERVICE);
        WifiInfo wifiInf = wifiMan.getConnectionInfo();
        macAddress = wifiInf.getMacAddress().replace(':', '-');
        ipAddress = wifiMan.getDhcpInfo().toString();
        int startIndex = ipAddress.indexOf(' ');
        int endIndex = ipAddress.indexOf(' ', startIndex + 1);
        ipAddress = ipAddress.substring(startIndex + 1, endIndex);
    }

    /* this function is to authenticate with the database
     * it returns the id_subject, if it is greater than 0
     * authentication was successful.
     */
    public static synchronized int authenticate() throws MalformedURLException, ProtocolException, IOException {
        Map<String, String> tempMap = new LinkedHashMap<String, String>();
        tempMap.put((String) tThis.getResources().getText(R.string.postAction), (String) tThis.getResources().getText(R.string.postAuthenticate));
        tempMap.put((String) tThis.getResources().getText(R.string.authUName), "username");
        tempMap.put((String) tThis.getResources().getText(R.string.authPWord), "password");
        String tempUrl = "https://ipaddress/health_monitoring/admin.php";
        return Integer.parseInt(post(tempUrl, tempMap));
    }

    /* this function is to register the server to the database
     * not sure of return value
     */
    public static synchronized int registerServer(String nameOfServer, String description) throws MalformedURLException, ProtocolException, IOException {
        setMACIPAddress();
        Map<String, String> tempMap = new LinkedHashMap<String, String>();
        tempMap.put((String) tThis.getResources().getText(R.string.postAction), (String) tThis.getResources().getText(R.string.postAddServer));
        tempMap.put((String) tThis.getResources().getText(R.string.addServerName), "Phone");
        tempMap.put((String) tThis.getResources().getText(R.string.addServerDescription), "Android");
        tempMap.put((String) tThis.getResources().getText(R.string.addServerURL), "");
        tempMap.put((String) tThis.getResources().getText(R.string.addServerIPAddress), ipAddress);
        tempMap.put((String) tThis.getResources().getText(R.string.addServerMAC), macAddress);

        String tempUrl = "https://ipaddress/health_monitoring/admin.php";
        return Integer.parseInt(post(tempUrl, tempMap));
    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                    return true;
            }
    };

    /**
     * Trust every server - dont check for any certificate
     */
    private static void trustAllHosts() {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[] {};
                    }

                    public void checkClientTrusted(X509Certificate[] chain,
                                    String authType) throws CertificateException {
                    }

                    public void checkServerTrusted(X509Certificate[] chain,
                                    String authType) throws CertificateException {
                    }
            } };

            // Install the all-trusting trust manager
            try {
                    SSLContext sc = SSLContext.getInstance("TLS");
                    sc.init(null, trustAllCerts, new java.security.SecureRandom());
                    HttpsURLConnection
                                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
            } catch (Exception e) {
                    e.printStackTrace();
            }
    }

    private static String post(String urlString, Map formParameters)
    throws MalformedURLException, ProtocolException, IOException {
        DataOutputStream ostream = null;

        trustAllHosts();
        URL tempUrl;
        StringBuffer buf = new StringBuffer();
        if(formParameters != null) {
            Set parameters = formParameters.keySet();
            Iterator it = parameters.iterator();

            for(int i = 0, paramCount = 0; it.hasNext(); i++) {
                String parameterName = (String) it.next();
                String parameterValue = (String) formParameters.get(parameterName);

                if(parameterValue != null) {
                    parameterValue = URLEncoder.encode(parameterValue);
                    if(paramCount > 0) {
                        buf.append("&");
                    }
                    buf.append(parameterName);
                    buf.append("=");
                    buf.append(parameterValue);
                    ++paramCount;
                }
            }
        }
        urlString = urlString + "?" + buf;
        Log.v("smartdbhelper url string", urlString);
        tempUrl = new URL(urlString);
        https = (HttpsURLConnection) tempUrl.openConnection();
        https.setHostnameVerifier(DO_NOT_VERIFY);
        Log.v("smartdbhelper adding post parameters", https.toString());
        https.setRequestMethod("POST");
        https.setDoInput(true);
        https.setDoOutput(true);
        ostream = new DataOutputStream(https.getOutputStream());
        ostream.writeBytes(buf.toString());

        if( ostream != null ) {
            ostream.flush();
            ostream.close();
        }
        Object contents = https.getContent();
        InputStream is = (InputStream) contents;
        StringBuffer buf2 = new StringBuffer();
        int c;
        while((c = is.read()) != -1) {
            buf2.append((char)c);
            Log.v("smartdbhelper bugger", buf2.toString());
        }
        //https.disconnect();
        return buf2.toString();
    }
}
