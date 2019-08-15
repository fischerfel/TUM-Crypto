// open the certificate
keyStore = KeyStore.getInstance("PKCS12");
fis = new FileInputStream(certificateFile);
keyStore.load(fis, clientCertPassword.toCharArray());


// create the SSL context
KeyManagerFactory kmf = KeyManagerFactory.getInstance("X509");
kmf.init(keyStore, clientCertPassword.toCharArray());
KeyManager[] keyManagers = kmf.getKeyManagers();

TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
tmf.init(trustStore);
TrustManager[] trustManagers = tmf.getTrustManagers();

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagers, trustManagers, null);


// perform the HTTP request
String result = null;
HttpURLConnection urlConnection = null;

try {
    URL requestedUrl = new URL(url);
    urlConnection = (HttpURLConnection) requestedUrl.openConnection();
    if(urlConnection instanceof HttpsURLConnection) {
        ((HttpsURLConnection)urlConnection)
             .setSSLSocketFactory(sslContext.getSocketFactory());
    }
    urlConnection.setRequestMethod("GET");
    urlConnection.setConnectTimeout(1500);
    urlConnection.setReadTimeout(1500);
    lastResponseCode = urlConnection.getResponseCode();
    result = IOUtil.readFully(urlConnection.getInputStream());
    lastContentType = urlConnection.getContentType();
} catch(Exception ex) {
    result = ex.toString();
} finally {
    if(urlConnection != null) {
        urlConnection.disconnect();
    }
}
