  SSLContext sslCtx = SSLContext.getInstance("TLS");
  sslCtx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
  SSLSocketFactory sslSF = sslCtx.getSocketFactory();
