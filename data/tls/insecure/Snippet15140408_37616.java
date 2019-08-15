SSLContext context = SSLContext.getInstance("TLSv1");
SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
SSLSocket sock = (SSLSocket) sslsocketfactory.createSocket("hostserver", 443);
String[] protocols = {"SSLv2Hello", "SSLv3", "TLSv1"}; 
