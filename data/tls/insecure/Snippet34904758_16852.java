SSLContext sslcontext = SSLContext.getInstance("TLSv1");
sslcontext.init(null, null, null);
SSLSocketFactory noSSLv3Factory = new TlsOnlySocketFactory(sc.getSocketFactory());
HttpsURLConnection.setDefaultSSLSocketFactory(noSSLv3Factory);
