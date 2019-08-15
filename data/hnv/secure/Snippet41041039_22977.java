HttpsURLConnection httpsConnection = null;              
SSLContext sslContext = SSLContext.getInstance("SSL");
TrustManager[] trustAll = new TrustManager[] {new TrustAllCertificates()};
sslContext.init(null, trustAll, new java.security.SecureRandom());

// Set trust all certificates context to HttpsURLConnection
HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

// Open HTTPS connection
URL urlUrl = new URL(url);
httpsConnection = (HttpsURLConnection) urlUrl.openConnection();

// Trust all hosts
httpsConnection.setHostnameVerifier(new TrustAllHosts());

// Connect
httpsConnection.connect();

SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(request), url);
soapConnection.close();
httpsConnection.disconnect();
