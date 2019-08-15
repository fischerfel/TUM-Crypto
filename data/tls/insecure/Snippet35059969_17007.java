 /**
 * Set up a connection to myservice.domain using HTTPS. An entire function
 * is needed to do this because myservice.domain has a self-signed certificate.
 * 
 * The caller of the function would do something like:
 * HttpsURLConnection urlConnection = setUpHttpsConnection("https://littlesvr.ca");
 * InputStream in = urlConnection.getInputStream();
 * And read from that "in" as usual in Java
 * 
 * Based on code from:
 * https://developer.android.com/training/articles/security-ssl.html#SelfSigned
 */
public static HttpsURLConnection setUpHttpsConnection(String urlString)
{
    try
    {
        // Load CAs from an InputStream
        // (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        // My CRT file that I put in the assets folder
        // I got this file by following these steps:
        // * Go to https://littlesvr.ca using Firefox
        // * Click the padlock/More/Security/View Certificate/Details/Export
        // * Saved the file as littlesvr.crt (type X.509 Certificate (PEM))
        // The MainActivity.context is declared as:
        // public static Context context;
        // And initialized in MainActivity.onCreate() as:
        // MainActivity.context = getApplicationContext();
        InputStream caInput = new BufferedInputStream(MainActivity.context.getAssets().open("littlesvr.crt"));
        Certificate ca = cf.generateCertificate(caInput);
        System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());

        // Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = new URL(urlString);
        HttpsURLConnection urlConnection = (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());

        return urlConnection;
    }
    catch (Exception ex)
    {
        Log.e(TAG, "Failed to establish SSL connection to server: " + ex.toString());
        return null;
    }
}
