KeyStore keystore = KeyStore.getInstance("pkcs12"); 
keystore.load(new FileInputStream("clientstore.p12"), keystorePassword); 
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm()); 
kmf.init(keystore, keystorePassword); 
KeyStore truststore = KeyStore.getInstance(KeyStore.getDefaultType()); 
truststore.load(new FileInputStream("truststore.jks"), truststorePassword); 

TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509"); 
tmf.init(truststore); 
SSLContext context = SSLContext.getInstance("TLS"); 
context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null); 

URL url = new URL("https://yourservice.com"); 
HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection(); 
SSLSocketFactory sslSocketFactory = context.getSocketFactory(); 
urlConnection.setSSLSocketFactory(sslSocketFactory); 
