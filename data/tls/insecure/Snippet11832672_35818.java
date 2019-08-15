SSLContext sslContext = SSLContext.getInstance("TLS");
KeyManagerFactory kFac = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
Provider pvdr = new sun.security.mscapi.SunMSCAPI();  
Security.insertProviderAt(pvdr,1);
KeyStore kStore = KeyStore.getInstance("Windows-MY",pvdr);
kStore.load(null,null);
kFac.init(kstore,null);
sslContext.init(kFac.getKeyManagers(), <a trust factory>.getTrustManagers(), new java.security.SecureRandom());
SSLSocketFactory sockFactory = SSLSocketFactory(sslContext);
SSLSocket sslSock = (SSLSocket)sockFactory.createSocket(<some destination host>, <some destination port>);
BufferedInputStream bInStr = new BufferedInputStream(sslSock.getInputStream());
bInStr.read(<the read arguments>);   <<< exception thrown in here
