public static SSLSocketFactory getSSLSocketFactory()
    throws IOException
{
    if(ssf_ == null)
    {
        javax.net.ssl.KeyManager kms[] = null;
        javax.net.ssl.TrustManager tms[] = null;
        SSLContext context = null;
        try
        {
            tms = CustomTrustManager.getTrustManagers();
            context = SSLContext.getInstance("TLS");
            context.init(kms, tms, null);
        }
        catch(GeneralSecurityException e)
        {
            IOException io = new IOException(e.getLocalizedMessage());
            io.setStackTrace(e.getStackTrace());
            throw io;
        }
        ssf_ = context.getSocketFactory();
    }
    return ssf_;
}
