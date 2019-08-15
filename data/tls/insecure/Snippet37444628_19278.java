context = SSLContext.getInstance("TLS");
kmf = KeyManagerFactory.getInstance("SunX509");
FileInputStream fin = new FileInputStream(storename);
ks = KeyStore.getInstance("JKS");
ks.load(fin, storepass);
context.init(kmf.getKeyManagers(), null, null);
