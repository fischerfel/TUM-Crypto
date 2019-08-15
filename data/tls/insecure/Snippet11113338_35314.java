String token="95076d2846e8979b46efd1884206a590d99d0f3f6139d947635ac4186cdc5942";

String host = "gateway.sandbox.push.apple.com";
int port = 2195;
String payload = "{\"aps\":{\"alert\":\"Message from Java o_O\"}}";

NotificationTest.verifyKeystore("res/myFile.p12", "password", false);
KeyStore keyStore = KeyStore.getInstance("PKCS12");
keyStore.load(getClass().getResourceAsStream("res/myFile.p12"), "password".toCharArray());

KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance("SunX509");
keyMgrFactory.init(keyStore, "password".toCharArray());

SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyMgrFactory.getKeyManagers(), null, null);
SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

SSLSocket sslSocket = (SSLSocket) sslSocketFactory.createSocket(host, port);
String[] cipherSuites = sslSocket.getSupportedCipherSuites();
sslSocket.setEnabledCipherSuites(cipherSuites);
sslSocket.startHandshake();

char[] t = token.toCharArray();
byte[] b = Hex.decodeHex(t);

OutputStream outputstream = sslSocket.getOutputStream();

outputstream.write(0);
outputstream.write(0);
outputstream.write(32);
outputstream.write(b);
outputstream.write(0);
outputstream.write(payload.length());
outputstream.write(payload.getBytes());

outputstream.flush();
outputstream.close();

System.out.println("Message sent .... ");
