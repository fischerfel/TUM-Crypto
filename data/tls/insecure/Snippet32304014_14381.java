public HttpsURLConnection setUpHttpsConnection(String urlString)
{
    try
    {
        // Load CAs from an InputStream
        CertificateFactory cf = CertificateFactory.getInstance("X.509");

        InputStream caInput = new BufferedInputStream(context.getAssets().open("server.crt"));
        Certificate ca = cf.generateCertificate(caInput);
        System.out.println("ca=" + ((java.security.cert.X509Certificate) ca).getSubjectDN());

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
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Create all-trusting host name verifier
        //  to avoid the following :
        //   java.security.cert.CertificateException: No name matching
        // This is because Java by default verifies that the certificate CN (Common Name) is
        // the same as host name in the URL. If they are not, the web service client fails.
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };
        // Install it
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        // Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = new URL(urlString);
        HttpsURLConnection urlConnection = null;
        urlConnection = (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

        return urlConnection;
    }
    catch (Exception ex)
    {
        Log.e("NetworkManager", "Failed to establish SSL connection to server: " + ex.toString());
        return null;
    }
}

/**
 * Represents an asynchronous login/registration task used to authenticate
 * the user.
 */
public class POSTTask extends AsyncTask<POSTRequest, Void, StringBuilder>
{
    POSTTask()
    {
    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected StringBuilder doInBackground(POSTRequest... params)
    {
        OutputStream os = null;

        try {
            HttpsURLConnection urlConnection = setUpHttpsConnection(params[0].url);
            //Sets the maximum time to wait for an input stream read to complete before giving up.
            urlConnection.setReadTimeout(30000);
            //Sets the maximum time in milliseconds to wait while connecting.
            urlConnection.setConnectTimeout(20000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(true);

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params[0].nameValuePairs);
            os = urlConnection.getOutputStream();
            formEntity.writeTo(os);

            InputStream in = urlConnection.getInputStream();
            StringBuilder ret = inputStreamToString(in);

            return ret;

        } catch (IOException e) {
            Log.i("NetworkError", e.toString());
        } catch (Exception e) {

        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException ex) {
                }
            }
        }             
        return null;
    }

    @Override
    protected void onPostExecute(StringBuilder result) {
    }

    @Override
    protected void onCancelled() {
    }
}
