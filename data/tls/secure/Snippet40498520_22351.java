ProviderInstaller.installIfNeeded(getApplicationContext());
SSLContext sslContext = SSLContext.getInstance("TLSv1.2");
sslContext.init(null, null, null);
mWebSocketClient.setWebSocketFactory(new DefaultSSLWebSocketClientFactory(sslContext));
mWebSocketClient.connect();
