TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());  
KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
keystore.load(ClassLoader.getSystemResourceAsStream("myKeystoreFile"), "password".toCharArray());  
trustManagerFactory.init(keystore);  

TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();  
SSLContext sslContext = SSLContext.getInstance("SSL");  
sslContext.init(null, trustManagers, null);  

HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
