KeyStore keyStore = KeyStore.getInstance("PKCS12");
keyStore.load(new FileInputStream("path/to/pfxFile"), "mypassword");
KeyManagerFactory keyManagerFactory = KeyManagerFactory
                .getInstance(KeyManagerFactory.getDefaultAlgorithm());
keyManagerFactory.init(keyStore, "mypassword");

KeyStore trustStore = KeyStore.getInstance("JKS");
trustStore.load(new FileInputStream("path/to/cacerts"), "changeit");
TrustManagerFactory trustManagerFactory = TrustManagerFactory
                .getInstance(TrustManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(trustStore);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(),
                new SecureRandom());

URL url = new URL("https://XXX.XXX.XXX.XXX/MyWebService");
connection = (HttpsURLConnection) url.openConnection();
connection.setSSLSocketFactory(sslContext.getSocketFactory());
connection.setRequestProperty("Content-Type", "text/xml");
connection.setRequestMethod("GET");
connection.setConnectTimeout(120000);
connection.connect();
