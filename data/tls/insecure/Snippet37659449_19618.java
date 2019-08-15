Properties systemProps = System.getProperties();
systemProps.put("javax.net.ssl.keyStore", keyStoreFile);
systemProps.put("javax.net.ssl.keyStorePassword", strKeyStorePassword);
systemProps.put("javax.net.ssl.keyStoreType", "JKS");
systemProps.put("javax.net.ssl.trustStore", keyStoreFile);
systemProps.put("javax.net.ssl.trustStoreType", "JKS");
systemProps.put("javax.net.ssl.trustStorePassword", strKeyStorePassword);
System.setProperties(systemProps); 
keyStore.load(new FileInputStream(keyStoreFile), strKeyStorePassword.toCharArray());
KeyManagerFactory kmFactory = KeyManagerFactory.getInstance("SunX509");
kmFactory.init(keyStore, strKeyStorePassword.toCharArray());
TrustManager[] trustManager;
TrustManagerFactory tmFactory = TrustManagerFactory.getInstance("SunX509");
tmFactory.init(keyStore);
trustManager = tmFactory.getTrustManagers();            // 
SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(kmFactory.getKeyManagers(), trustManager, null);            
sslSocketFactory = sslContext.getSocketFactory();
HttpURLConnection httpConnection = null;
java.net.URL url = new URL(strURL);
if (url.getProtocol().equalsIgnoreCase("https")) {
httpConnection = (HttpURLConnection) url.openConnection();      
}
System.out.println("\nRequesting POST for URL -  " + url);
httpConnection.setRequestProperty("SOAPAction","POST");
httpConnection.setDoOutput(true);
httpConnection.setDoInput(true);
httpConnection.setRequestMethod("POST");
OutputStreamWriter writer = new OutputStreamWriter(httpConnection.getOutputStream());//Exception occurs here
writer.write(strReq);
writer.close();
String strResponse = null; 
if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
strResponse = getSOAPResponseString(httpConnection.getInputStream());
}
System.out.println(httpConnection.getResponseCode());          
System.out.println(httpConnection.getResponseMessage());
