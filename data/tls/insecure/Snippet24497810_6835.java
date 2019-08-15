KeyStore myStore = KeyStore.getInstance("JKS");
InputStream keyInputx = new FileInputStream("C:\\myStore.jks");
myStore.load(keyInputx, "xxx".toCharArray());
KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
keyInputx.close();
/*Enumeration enumeration = myStore.aliases();
 while (enumeration.hasMoreElements()) {
     String alias = (String) enumeration.nextElement();
     System.out.println("alias name: " + alias);
     Certificate certificate = myStore.getCertificate(alias);
     System.out.println(certificate.toString());
 }*/
keyManagerFactory.init(myStore, "xxx".toCharArray());
SSLContext context = SSLContext.getInstance("TLS");
context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
SSLSocketFactory sockFact = context.getSocketFactory();
