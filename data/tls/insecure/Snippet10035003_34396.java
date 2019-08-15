
SSLContext context = SSLContext.getInstance("TLS");
context.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
