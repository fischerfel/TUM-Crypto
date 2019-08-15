SSLSocketFactory sslSktFactory = SSLContext.getInstance("TLS").getSocketFactory();

httpsUrlConnection.setSSLSocketFactory(new CustomSSLSocketFactory(sslSktFactory ));
