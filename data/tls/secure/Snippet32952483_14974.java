SSLContext sslcontext = SSLContext.getInstance("TLSv1.2");
            sslcontext.init(kmf.getKeyManagers(), tmf.getTrustManagers(),null);
sslsf = new SSLConnectionSocketFactory(sslcontext, new String[] { "TLSv1.2" }, null,
            SSLConnectionSocketFactory.getDefaultHostnameVerifier());
...
//called multiple times
CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(sslsf).build();