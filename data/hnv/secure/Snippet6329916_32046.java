HttpClient client = new DefaultHttpClient();

SSLSocketFactory sf = (SSLSocketFactory) client.getConnectionManager().getSchemeRegistry().getScheme("https").getSocketFactory();
sf.setHostnameVerifier(new AllowAllHostnameVerifier());
