// Load the server keystore
KeyStore keyStore = KeyStore.getInstance("BKS");
keyStore.load(context.getResources().openRawResource(R.raw.sys), certificatePassword.toCharArray());

// Create a custom trust manager that accepts the server self-signed certificate
TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
trustManagerFactory.init(keyStore);

// Create the SSLContext for the SSLSocket to use
SSLContext sslctx = SSLContext.getInstance("TLS");
sslctx.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

// Create SSLSocketFactory
SSLSocketFactory factory = sslctx.getSocketFactory();

// Create socket using SSLSocketFactory
client = factory.createSocket(ipAddress, port); <-- Here application hangs
