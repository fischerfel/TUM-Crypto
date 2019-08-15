File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "certificate.pfx");
KeyStore clientStore = KeyStore.getInstance("PKCS12");
clientStore.load(new FileInputStream(file), "password".toCharArray());

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(clientStore, "password".toCharArray());
KeyManager[] kms = kmf.getKeyManagers();

SSLContext sslContext = null;
sslContext = SSLContext.getInstance("TLS");
sslContext.init(kms, null, new SecureRandom());            

HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
