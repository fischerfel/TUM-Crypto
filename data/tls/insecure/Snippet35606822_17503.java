private static SSLSocketFactory factory() throws NoSuchAlgorithmException, KeyManagementException 
{
    SSLSocketFactory factorySingleton;
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, getTrustManager(), null);
    factorySingleton = ctx.getSocketFactory();

    return factorySingleton;
}
