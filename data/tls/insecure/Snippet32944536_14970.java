URL url = new URL("https://bankurlservice"); 
HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection(); 
KeyStore ksCACert = KeyStore.getInstance(KeyStore.getDefaultType()); 
ksCACert.load(new FileInputStream("keystore path"), "keystorepass".toCharArray()); 
TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509"); 
tmf.init(ksCACert);
SSLContext context = SSLContext.getInstance("TLS"); 
context.init(null, tmf.getTrustManagers(), null);
SSLSocketFactory sslSocketFactory = context.getSocketFactory();
urlConnection.setSSLSocketFactory(sslSocketFactory); 
BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); 
String res = "";
String inputLine; while ((inputLine = in.readLine()) != null) { res += inputLine; } in.close(); 
