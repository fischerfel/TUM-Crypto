SSLContext context = SSLContext.getInstance("TLSv1");
context.init(new KeyManager[] { new FilteredKeyManager((X509KeyManager)originalKeyManagers[0], desiredCertsForConnection) },
    trustManagerFactory.getTrustManagers(), new SecureRandom());
