    KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
    kmf.init(aboveKeyStore, "changeme".toCharArray());
    sslContext = SSLContext.getInstance("SSLv3");
    sslContext.init(kmf.getKeyManagers(), null, null);
