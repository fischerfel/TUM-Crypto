System.setProperty("sun.security.ssl.allowUnsafeRenegotiation", "true");
KeyStore ks = KeyStore.getInstance("JKS");
ks.load(new FileInputStream("/etc/certificates/fdms/WS1001237590._.1.ks"), "DV8u4xRVDq".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
kmf.init(ks, "DV8u4xRVDq".toCharArray());
SSLContext sc = SSLContext.getInstance("TLS");
sc.init(kmf.getKeyManagers(), null, null);
