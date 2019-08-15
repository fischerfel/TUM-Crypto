InputStream clientTruststoreIs = getResources().openRawResource(R.raw.truststore);
KeyStore trustStore = null;
trustStore = KeyStore.getInstance("BKS");
trustStore.load(clientTruststoreIs, "xxxxx".toCharArray());

System.out.println("Loaded server certificates: " + trustStore.size());

TrustManagerFactory tmf = null;
tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
tmf.init(trustStore);

InputStream keyStoreStream = getResources().openRawResource(R.raw.client);
KeyStore keyStore = null;
keyStore = KeyStore.getInstance("BKS");
keyStore.load(keyStoreStream, "xxxxxx".toCharArray());


KeyManagerFactory kmf = null;
kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
kmf.init(keyStore, "xxxxxxxxx".toCharArray());

SSLContext ctx = SSLContext.getInstance("SSL");
ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
stomp = new Stomp("tcp://" + sp.getString("host", "default.host.it")+   ":"+ sp.getString("port", "61614"));
stomp.setSslContext(ctx);
