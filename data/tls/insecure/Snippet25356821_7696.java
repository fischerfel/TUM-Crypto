SSLSocketFactory sf = new SSLSocketFactory(
SSLContext.getInstance("TLS"),
SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
Scheme sch = new Scheme("https", 443, sf);
httpclient.getConnectionManager().getSchemeRegistry().register(sch);

HttpGet httpget = new HttpGet("https://host/");
