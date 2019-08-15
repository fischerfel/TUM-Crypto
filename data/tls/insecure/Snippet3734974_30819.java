KeyStore ks = KeyStore.getInstance("PKCS12");
FileInputStream fis = new FileInputStream("/path/to/file.p12");
ks.load(fis, "password".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, "password".toCharArray());
SSLContext sc = SSLContext.getInstance("TLS");
sc.init(kmf.getKeyManagers(), null, null);
