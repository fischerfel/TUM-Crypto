SSLContext sslContext=SSLContext.getInstance("SSL","SunJSSE");
sslContext.init(null, new TrustManager[] {myTm}, null);
SSLSocketFactory sf = sslContext.getSocketFactory();
