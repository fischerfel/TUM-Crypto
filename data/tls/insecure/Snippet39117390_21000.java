//init keyManagerFactory and trustManager
SSLContext sslContext = SSLContext.getInstance("TLS");
sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] { trustManager }, new SecureRandom());
SSLSocketFactory socketFactory = sslContext.getSocketFactory();
SSLSocket socket = (SSLSocket) socketFactory.createSocket(new Socket(ipAddress, port), ipAddress, port, false);
socket.startHandshake();

DataInputStream in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
... 

int toRead = xxxx; //received from server response
List<Byte> bytes = new ArrayList<>();
while(toRead > 0) {
    byte b = in.readByte();
    bytes.add(b);
    toRead--;
}
