SSLContext sslcontext = SSLContext.getInstance("TLSv1");
sslcontext.init(null, null, null);
//  NoSSLv3SocketFactory is @bhavit-s-sengar's http://stackoverflow.com/a/29946540/8524
SSLSocketFactory noSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());

NetHttpTransport.Builder netTransportBuilder = new NetHttpTransport.Builder();
netTransportBuilder.setSslSocketFactory(noSSLv3Factory);
HTTP_TRANSPORT = netTransportBuilder.build();
