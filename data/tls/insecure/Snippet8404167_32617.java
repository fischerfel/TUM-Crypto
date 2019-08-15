SSLContext sslContext = SSLContext.getInstance("SSL");
sslContext.init(null, trustManagers, null);
SSLContext.setDefault(sslContext);
