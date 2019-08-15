public class MyHttpsGet extends AsyncTask<String, Void, String> {

Context context;

int cert;
boolean allowHost;
private String username;
private String password;

//this is used if you need a password and username
//mainly for logins to a webserver
public MyHttpsGet(String username, String password, Context context, int cert)
{
    this.context = context;
    this.cert = cert;
    this.allowHost = allowHost;
    this.username = username;
    this.password = password;

}

//used for image downloading
public MyHttpsGet(){}

@Override
protected String doInBackground(String... params) {
    String url = params[0];
    return httpsDownloadData(url, context, cert);
}

public String httpsDownloadData (String urlString, Context context, int certRawResId)
{
    String respone = null;

    try {
        // build key store with ca certificate
        KeyStore keyStore = buildKeyStore(context, certRawResId);

        // Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

        // Create an SSLContext that uses our TrustManager
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Create a connection from url
        URL url = new URL(urlString);
        if (username != null) {
            Authenticator.setDefault(new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(username, password.toCharArray());
                }
            });
        }
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
        urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());

        int statusCode = urlConnection.getResponseCode();
        Log.d("Status code: ", Integer.toString(statusCode));


        InputStream inputStream = urlConnection.getInputStream();
        if (inputStream != null) {
            respone = streamToString(inputStream);
            inputStream.close();
        }

    }catch (IOException e) {
        e.printStackTrace();
    } catch (CertificateException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (KeyStoreException e) {
        e.printStackTrace();
    } catch (KeyManagementException e) {
        e.printStackTrace();
    }

    Log.d("MyHttps Respones: ", respone);
    return respone;
}

private static KeyStore buildKeyStore(Context context, int certRawResId) throws KeyStoreException, CertificateException, NoSuchAlgorithmException, IOException {
    // init a default key store
    String keyStoreType = KeyStore.getDefaultType();
    KeyStore keyStore = KeyStore.getInstance(keyStoreType);
    keyStore.load(null, null);

    // read and add certificate authority
    Certificate cert = readCert(context, certRawResId);
    keyStore.setCertificateEntry("ca", cert);

    return keyStore;
}

private static Certificate readCert(Context context, int certResourceId) throws CertificateException, IOException {

    // read certificate resource
    InputStream caInput = context.getResources().openRawResource(certResourceId);

    Certificate ca;
    try {
        // generate a certificate
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        ca = cf.generateCertificate(caInput);
    } finally {
        caInput.close();
    }

    return ca;
}

//this is used for downloading strings from an http or https connection
private String streamToString(InputStream is) throws IOException {

    StringBuilder sb = new StringBuilder();
    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
    String line;
    while ((line = rd.readLine()) != null) {
        sb.append(line);
    }

    return sb.toString();
}
