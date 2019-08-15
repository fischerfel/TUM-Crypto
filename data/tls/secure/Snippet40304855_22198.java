X509Certificate rootCertificate = ...;
X509Certificate intermediateCertificate = ...;
X509Certificate myPublicCertificate = ...;
KeyStore myPrivateKey = ...;


KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
keyManagerFactory.init(myPrivateKey, "password");
SSLContext context = SSLContext.getInstance("TLSv1.2");
context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());

SSLSocketFactory socketFactory = context.getSocketFactory();
HttpsURLConnection.setDefaultSSLSocketFactory(socketFactory);
URL url = new URL(urlPath);
HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
