String keyStoreName = "<keyStoreName>";
String password = "<password>";
URL url = new URL("<url string>");
// Get KeyStore
KeyStore ks = null;
FileInputStream fis = null;
try {
    ks = KeyStore.getInstance("JKS");
    char[] passwordArray = password.toCharArray();
    fis = new java.io.FileInputStream(keyStoreName);
    ks.load(fis, passwordArray);
    fis.close();
} catch (Exception e) {
    e.printStackTrace();
} finally {
    if (fis != null) {
        fis.close();
    }
}
// Get SSLSocketFactory
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
keyManagerFactory.init(ks, password.toCharArray());
SSLContext context = SSLContext.getInstance("TLS");
context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
// Process GET Request
SSLSocketFactory sslFactory = context.getSocketFactory();
HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
con.setSSLSocketFactory(sslFactory);
con.setRequestMethod("GET");
con.addRequestProperty("x-ms-version", "2013-08-01");
InputStream responseStream = (InputStream) con.getContent();
String response = getStringFromInputStream(responseStream);
responseStream.close();
