KeyStore clientStore = KeyStore.getInstance("PKCS12");
clientStore.load(new FileInputStream(KEY_STORE_PATH), "xxxxxx".toCharArray());

KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());

kmf.init(clientStore, "xxxxxx".toCharArray());

KeyManager[] kms = kmf.getKeyManagers();

KeyStore trustStore = KeyStore.getInstance("JKS");

trustStore.load(new FileInputStream(TRUST_STORE_PATH), "xxxxxx".toCharArray());

TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

tmf.init(trustStore);

TrustManager[] tms = tmf.getTrustManagers();

SSLContext sslContext = null;

sslContext = SSLContext.getInstance("TLS");

sslContext.init(kms, tms, new SecureRandom());

HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());`

connection = (HttpsURLConnection) url.openConnection();
