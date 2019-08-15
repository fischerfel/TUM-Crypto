try {
  SSLSocket sslsocket = (SSLSocket) sslsocketfactory.createSocket();
  sslsocket.connect(new InetSocketAddress(SERVER_IP, UPLOAD_PORT), 2000);

  OutputStream outputStream = sslsocket.getOutputStream();

  DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
  dataOutputStream.writeInt(DEVICE_ID);
  dataOutputStream.writeLong(FILE_LENGTH);

  MessageDigest md = MessageDigest.getInstance("MD5");
  DigestOutputStream dos = new DigestOutputStream(outputStream, md);

  InputStream readingsInputStream = new FileInputStream(FILE_NAME);

  int count;
  byte[] buffer = new byte[10 * 1024];
  while ((count = readingsInputStream.read(buffer)) > 0) {
    dos.write(buffer, 0, count);
  }
  readingsInputStream.close();

  byte[] md5 = md.digest();
  byte[] serverMD5 = new byte[16];

  DataInputStream dataInputStream = new DataInputStream(sslsocket.getInputStream());

  for (int i = 0;i<16;i++) {
    serverMD5[i] = dataInputStream.readByte();
    if (md5[i] != serverMD5[i]) throw new Exception("MD5 mismatch");
  }

  sslsocket.close();
} catch (Exception e) {
  ...
}
