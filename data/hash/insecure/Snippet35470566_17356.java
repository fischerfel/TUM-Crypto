try {
  SSLSocket sslSocket = (SSLSocket) sslServerSocket.accept();

  InputStream inputStream = sslSocket.getInputStream();

  DataInputStream dataInputStream = new DataInputStream(inputStream);
  int deviceID = dataInputStream.readInt();
  long fileLength = dataInputStream.readLong();

  MessageDigest md = MessageDigest.getInstance("MD5");
  DigestInputStream dis = new DigestInputStream(inputStream, md);

  OutputStream readingsOutputStream = new FileOutputStream("Device"+deviceID+".txt", false);

  int count;
  byte[] buffer = new byte[1];
  do {
    count = dis.read(buffer);
    readingsOutputStream.write(buffer, 0, count);
    fileLength -= count;
  } while (fileLength > 0);

  readingsOutputStream.close();

  byte[] md5 = md.digest();

  DataOutputStream md5OutputStream = new DataOutputStream(sslSocket.getOutputStream());
  for (int i = 0;i<16;i++) md5OutputStream.writeByte(md5[i]);

  sslSocket.close();
} catch (Exception e) {
  ...
}
