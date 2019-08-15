    javax.net.ssl.SSLContext context = javax.net.ssl.SSLContext.getInstance("SSL");
    context.init(getKeyManagers(), (TrustManager[]) getKeyManagers(), null);
    SSLContext.setDefault(context);
