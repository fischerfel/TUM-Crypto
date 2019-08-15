context = SSLContext.getInstance("TLS");
---->  context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), new SecureRandom());

java.lang.RuntimeException: forget something!
at org.bouncycastle.jce.provider.JDKKeyStore$StoreEntry.getObject(JDKKeyStore.java:314)
at org.bouncycastle.jce.provider.JDKKeyStore.engineGetKey(JDKKeyStore.java:604)
at java.security.KeyStoreSpi.engineGetEntry(KeyStoreSpi.java:376)
at java.security.KeyStore.getEntry(KeyStore.java:734)
at org.apache.harmony.xnet.provider.jsse.KeyManagerImpl.<init>(KeyManagerImpl.java:72)
