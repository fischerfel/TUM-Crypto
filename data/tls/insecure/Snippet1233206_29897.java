int port = 2195;
String hostname = "gateway.sandbox.push.apple.com";
char[] passwKey = "password".toCharArray();

KeyStore ts = KeyStore.getInstance("PKCS12");
ts.load(new FileInputStream("/path/to/file/Cert.p12"), passwKey);
KeyManagerFactory tmf = KeyManagerFactory.getInstance("SunX509");
tmf.init(ts, passwKey);

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(tmf.getKeyManagers(), null, null);
SSLSocketFactory factory = sslContext.getSocketFactory();

SSLSocket socket = (SSLSocket) factory.createSocket(hostname,port); 
String[] suites = socket.getSupportedCipherSuites();
socket.setEnabledCipherSuites(suites);
//start handshake
socket.startHandshake(); 
