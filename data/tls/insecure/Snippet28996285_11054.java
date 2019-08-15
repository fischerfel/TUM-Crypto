File keyStoreFile = new File("./myKeyStore");
KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
ks.load(new FileInputStream(keyStoreFile), "keyStorePassword".toCharArray());
TrustManagerFactory tmf = TrustManagerFactory.getInstance("X509");
tmf.init(ks);
SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, tmf.getTrustManagers(), new java.security.SecureRandom());
