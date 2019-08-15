javax.net.ssl.KeyManager[] kms = new javax.net.ssl.KeyManager[]{
     new MyKeyManager(keystore, keypass, hosts)
};
TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
tmf.init(keystore);
javax.net.ssl.TrustManager[] tms = tmf.getTrustManagers();
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(kms, tms, null);
