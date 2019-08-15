// Load the truststore that includes self-signed cert as a "trusted" entry.
KeyStore truststore;
truststore = KeyStore.getInstance("BKS");
InputStream in = getActivity().getResources().openRawResource(R.raw.truststore);
truststore.load(in, "Your Password".toCharArray());

TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(truststore);

// Create custom SSL context that incorporates that truststore
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

connection.setSSLSocketFactory(sslContext.getSocketFactory());
