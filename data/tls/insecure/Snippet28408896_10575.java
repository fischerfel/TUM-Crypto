public class CustomImageDownaloder extends BaseImageDownloader {
public static final String TAG = CustomImageDownaloder.class.getName();


public CustomImageDownaloder(Context context
, int connectTimeout, int             
readTimeout) 
{
        super(context, connectTimeout, readTimeout);
}

@Override
protected InputStream getStreamFromNetwork(String imageUri, Object extra) throws IOException {

    URL url = null;
    try {
        url = new URL(imageUri);
    } catch (MalformedURLException e) {
        Log.e(TAG, e.getMessage(), e);
    }
    HttpURLConnection http = null;
    http.setRequestProperty("Authorization", "Basic " + encodeCredentials());

    if (Scheme.ofUri(imageUri) == Scheme.HTTPS) {
        trustAllHosts();
        HttpsURLConnection https = (HttpsURLConnection) url
                .openConnection();

        https.setHostnameVerifier(DO_NOT_VERIFY);
        http = https;
        http.connect();
    } else {
        http = (HttpURLConnection) url.openConnection();
    }
    http.setConnectTimeout(connectTimeout);
    http.setReadTimeout(readTimeout);
    return new FlushedInputStream(new BufferedInputStream(
            http.getInputStream()));
}

// always verify the host - dont check for certificate
final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
    @Override
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
        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] x509Certificates,
                String s) throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] x509Certificates,
                String s) throws java.security.cert.CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[] {};
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

public static String encodeCredentials() {
    try {
        String userpass="";
        String auth = Base64.encodeToString(userpass.getBytes("UTF-8"), Base64.NO_WRAP);
        return auth;
    } catch (Exception ignored) {
        Log.e(TAG, ignored.getMessage(), ignored);
    }
    return "";
}
