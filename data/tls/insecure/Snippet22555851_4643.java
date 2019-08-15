public EmblSSLSocketFactory()
 {
try
{
  SSLContext ctx = SSLContext.getInstance("TLS");
  ctx.init(null, new TrustManager[]
  {
    new DummyTrustmanager()
  }, new SecureRandom());
  socketFactory = ctx.getSocketFactory();
}
catch (Exception ex)
{
  ex.printStackTrace(System.err); /* handle exception */
}
