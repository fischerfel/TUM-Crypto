....
    SocketAddress sa = new InetSocketAddress(ip, port);
....
    SSLContext sslContext = SSLContext.getInstance("SSL");
....
      sslContext.init(.........., new SecureRandom());

      SSLSocketFactory socketFactory = sslContext.getSocketFactory();
      this.clientSock = socketFactory.createSocket();
      this.clientSock.connect(sa, this.ConnectTimeout);       
      this.clientSock.setSoTimeout(this.RecvTimeout);
      this.clientSock.setReuseAddress(true);
....

public boolean writeTo(String procname, BufferedOutputStream out, byte[] data)
{....
    out.write(data, 0, data.length);
    out.flush();
....}
