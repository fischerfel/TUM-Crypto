SchemeRegistry   registry         = new SchemeRegistry();
SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
sslSocketFactory.setHostnameVerifier(SSLSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
registry.register(new Scheme("https", sslSocketFactory, 443));
SingleClientConnManager manager = new SingleClientConnManager(params, registry);
