String path = "/Library/Java/JavaVirtualMachines/jdk1.7.0_71.jdk/Contents/Home/jre/lib/security/cacerts"
char[] trustStorePassword = <password>.toCharArray();

InputStream trustStream = new FileInputStream(path);
KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
trustStore.load(trustStream, trustStorePassword);
trustStream.close()

TrustManagerFactory trustFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustFactory.init(trustStore);
TrustManager[] trustManagers = trustFactory.getTrustManagers();
SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
sslContext.init(null, trustManagers, null);

URL url = new URL(<url-to-server>)
HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
String userCredentials = "uname:passwd"
String basicAuth = "Basic " + new String(userCredentials.getBytes().encodeAsBase64())
con.setAllowUserInteraction(true)
con.setRequestProperty("Authorization", basicAuth);
con.setRequestMethod("POST");
con.setRequestProperty("Content-Type", "text/xml");
con.setSSLSocketFactory(sslContext.getSocketFactory());
con.setDoOutput(true)

String urlParameters = "some-xml-data-as-string"
OutputStream wr = con.getOutputStream()
wr.write(urlParameters.getBytes("UTF-8"));
wr.flush();
wr.close();
int responseCode = con.getResponseCode(); //this is where i get exception
