SSLContext ctx = SSLContext.getInstance("TLSv1");
ctx.init(null, tmf.getTrustManagers(), null);
