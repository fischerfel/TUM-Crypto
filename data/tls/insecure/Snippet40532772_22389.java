SSLContext context = SSLContext.getInstance("TLS");
TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(dummyTrustStore);

X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];

SavingTrustManager savingTrustManager = new SavingTrustManager(defaultTrustManager);
context.init(null, new TrustManager[] { savingTrustManager }, null);
SSLSocketFactory factory = context.getSocketFactory();
