KeyStore keyStore_manager = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore_manager.load(_context.getAssets().open(your_keystore),password.toCharArray());
KeyStore keyStore_trust = KeyStore.getInstance(KeyStore.getDefaultType());
keyStore_trust.load(_context.getAssets().open(your_trustore),password.toCharArray());
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keyStore_manager, password.toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(keyStore_trust);
HttpsURLConnection.setDefaultHostnameVerifier(new NullHostNameVerifier());
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagerFactory.getKeyManagers(),tmf.getTrustManagers(), null);
HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
URL url = new URL(your_url);
HttpsURLConnection urlConnection = (HttpsURLConnection) requestedUrl.openConnection();
if (urlConnection instanceof HttpsURLConnection)
{
    ((HttpsURLConnection) urlConnection).setSSLSocketFactory(sslContext
            .getSocketFactory());
}
urlConnection.setRequestMethod("GET");
urlConnection.setConnectTimeout(1500);
urlConnection.connect();
String data = "";
int http_status = urlConnection.getResponseCode();
if (http_status == 200)
{
//read data sent from server
    InputStream response = new BufferedInputStream(

    urlConnection.getInputStream());

    int bytesRead = -1;
    byte[] buffer = new byte[30 * 1024];
    while ((bytesRead = response.read(buffer)) > 0)
    {
        data = new String(buffer, 0, bytesRead);
    }

}
urlConnection.disconnect();
