//my database helper class
public class SmartDBHelper {
    private static SmartDBHelper sDBHObject;

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

    /* this function is to authenticate with the database
     * it returns the id_subject, if it is greater than 0
     * authentication was successful.
     */
    public static synchronized int authenticate(String uName, String pWord) {
        Map<String, String> tempMap = new LinkedHashMap<String, String>();
        tempMap.put("action", "authentication");
        tempMap.put("username", "uName");
        tempMap.put("password", "pWord");
        try {
            String tempUrl = "https://ipaddress/health_monitoring/admin.php";
            String result = post(tempUrl, tempMap);
            Log.v("smartdbhelper post result", result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
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
        tempUrl = new URL(urlString);
        HttpsURLConnection https = (HttpsURLConnection) tempUrl.openConnection();
        https.setHostnameVerifier(DO_NOT_VERIFY);

        https.setRequestMethod("POST");
        https.setDoInput(true);
        https.setDoOutput(true);
        ostream = new DataOutputStream(https.getOutputStream());

        if(formParameters != null) {
            Set parameters = formParameters.keySet();
            Iterator it = parameters.iterator();
            StringBuffer buf = new StringBuffer();

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
            Log.v("smartdbhelper adding post parameters", buf.toString());
            Log.v("smartdbhelper adding post parameters", https.toString());
            ostream.writeBytes(buf.toString());
        }

        if( ostream != null ) {
            ostream.flush();
            ostream.close();
        }
        Object contents = https.getContent();
        InputStream is = (InputStream) contents;
        StringBuffer buf = new StringBuffer();
        int c;
        while((c = is.read()) != -1) {
            buf.append((char)c);
            Log.v("smartdbhelper bugger", buf.toString());
        }
        https.disconnect();
        return buf.toString();
    }
}
