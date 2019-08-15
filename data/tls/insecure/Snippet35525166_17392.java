javax.net.ssl.SSLContext ctx = javax.net.ssl.SSLContext.getInstance("TLS");
ctx.init(kmf.getKeyManagers(), null, null);
javax.net.ssl.SSLContext.setDefault(ctx);
