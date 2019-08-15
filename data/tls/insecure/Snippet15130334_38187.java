public class MyTrustAllSslSocketFactory extends SSLSocketFactory
{  
  private SSLSocketFactory    m_sslSocketFactory;

  public MyTrustAllSslSocketFactory() throws Exception
  {
    SSLContext sslContext = SSLContext.getInstance("TLS");
    sslContext.init(null, new TrustManager[] { new TrustAllX509Manager() }, new SecureRandom());
    m_sslSocketFactory = sslContext.getSocketFactory();
  }

  @Override
  public Socket createSocket(Socket s, String host, int port, boolean autoClose) throws IOException
  {
    return m_sslSocketFactory.createSocket(s, host, port, autoClose);
  }

  @Override
  public String[] getDefaultCipherSuites()
  {
    return m_sslSocketFactory.getDefaultCipherSuites();
  }

  @Override
  public String[] getSupportedCipherSuites()
  {
    return m_sslSocketFactory.getSupportedCipherSuites();
  }

  @Override
  public Socket createSocket(String host, int port) throws IOException, UnknownHostException
  {
    return m_sslSocketFactory.createSocket(host, port);
  }

  @Override
  public Socket createSocket(InetAddress host, int port) throws IOException
  {
    return m_sslSocketFactory.createSocket(host, port);
  }

  @Override
  public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException
  {
    return m_sslSocketFactory.createSocket(host, port, localHost, localPort);
  }

  @Override
  public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException
  {
    return m_sslSocketFactory.createSocket(address, port, localAddress, localPort);
  }
}
