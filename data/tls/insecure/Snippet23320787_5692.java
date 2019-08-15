...
SSLContext context = SSLContext.getInstance("TLS");
context.init(null, trustManager.getTrustManagers(), null);

HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
connection.setSSLSocketFactory(context.getSocketFactory());
