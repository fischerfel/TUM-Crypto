SSLSocketFactory sslSocketFactory = SSLSocketFactory.getSocketFactory();
sslSocketFactory.setHostnameVerifier(SSLSocketFactory.STRICT_HOSTNAME_VERIFIER);
registry.register(new Scheme("https", sslSocketFactory, 443));
