
SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);
