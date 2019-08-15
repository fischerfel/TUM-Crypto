    String certFilePath= "D:\\certificates\\A1.pfx";
    String jksFilepath= "D:\\certificates\\clientcert.jks";
    System.setProperty("javax.net.ssl.keyStore",certFilePath);
    System.setProperty("javax.net.ssl.keyStoreType","PKCS12");
    System.setProperty("javax.net.ssl.keyStorePassword","XXXXXX");

    System.setProperty("javax.net.ssl.trustStore",jksFilepath );
    System.setProperty("javax.net.ssl.trustStorePassword", "XXXXXX");


KeyStore ks = KeyStore.getInstance("JKS");
FileInputStream fis = new FileInputStream(certFilePath);
ks.load(fis, "xxxxx".toCharArray());
KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(ks, "xxxxxx".toCharArray());
SSLContext sc = SSLContext.getInstance("TLS");
sc.init(kmf.getKeyManagers(), null, null);
