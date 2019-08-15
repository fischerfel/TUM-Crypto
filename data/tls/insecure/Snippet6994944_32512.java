KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
File ksFile = new File(keystorePath);
in = new FileInputStream(ksFile);
ks.load(in, "changeit".toCharArray());
X509Certificate cert = (X509Certificate) ks.getCertificate(certificateAlias);

SSLContext sc = SSLContext.getInstance("TLS");
sc.init(null, trustAllCerts, new java.security.SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

HttpsURLConnection con = (HttpsURLConnection) (new URL(urlString)).openConnection();
con.connect();
con.getInputStream();
con.disconnect();
