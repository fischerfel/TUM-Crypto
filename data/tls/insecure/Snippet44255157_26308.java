//create the SSLContext with your key manager and trust
//manager, and get your socket factory from the context:
SSLContext ctx = SSLContext.getInstance("SSL");
ctx.init(km, tm, null);
SSLSocketFactory factory = ctx.getSocketFactory();
