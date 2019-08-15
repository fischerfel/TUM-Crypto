...
HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
httpClientPolicy.setAllowChunking(false);
httpClientPolicy.setAutoRedirect(true);
httpClientPolicy.setConnection(ConnectionType.KEEP_ALIVE);

String proxyUrl = "http://proxy.com";
String proxyPortString = "8080";
HTTPConduit http = (HTTPConduit)client.getConduit();
SSLContext sslContext = SSLContext.getInstance("TLSv1");
sslContext.init(null, null, null);
HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
TLSClientParameters tlsClientParameters = new TLSClientParameters();
tlsClientParameters.setUseHttpsURLConnectionDefaultSslSocketFactory(true);

http.setTlsClientParameters(tlsClientParameters);
http.setClient(httpClientPolicy);
