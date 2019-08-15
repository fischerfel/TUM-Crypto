SSLContext context = SSLContext.getInstance("SSL");
context.init(new KeyManager[] {}, new TrustManager[] {}, new SecureRandom());
SSLSocketFactory sf = new SSLSocketFactory(context, SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
client.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, sf));
