initFactory() {
  SSLContext context = SSLContext.getInstance("TLSv1.2");
  context.init(null, null, null);
  sslFactory = context.getSocketFactory();
}
