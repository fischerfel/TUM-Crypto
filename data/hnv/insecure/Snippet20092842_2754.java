DefaultHttpClient httpClient = new DefaultHttpClient();
SSLContext ctx = SSLContext.getInstance("SSLv3");
TrustManager[] trustManagers = getTrustManagers("jks", new FileInputStream(new File("C:\\SSLKeyStore.ks")), "changeit");
ctx.init(null, trustManagers, new SecureRandom());
System.out.println("Context Protocol - " + ctx.getProtocol());

SSLSocketFactory factory = new SSLSocketFactory(ctx);
factory.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

ClientConnectionManager manager = httpClient.getConnectionManager();
manager.getSchemeRegistry().register(new Scheme("https", 443, factory));

HttpGet httpget = new HttpGet("https://localhost:8844/getData");

System.out.println("executing request" + httpget.getRequestLine());

HttpResponse response = httpClient.execute(httpget);
