KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
String algorithm = TrustManagerFactory.getDefaultAlgorithm();
TrustManagerFactory tmf = TrustManagerFactory.getInstance(algorithm);
tmf.init(keyStore);

SSLContext context = SSLContext.getInstance("TLS");
context.init(null, tmf.getTrustManagers(), null);

URL url = new URL("https://myaddress.com/");
HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
urlConnection.setSSLSocketFactory(context.getSocketFactory());
urlConnection.setRequestProperty("KeepAlive", true);
InputStream in = urlConnection.getInputStream();
//posting content and handling response
finally {
    in.close();
    urlConnection.disconnect();
}
