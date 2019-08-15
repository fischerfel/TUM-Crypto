public class CustomTtrustManager implements X509TrustManager
{
    public boolean isClientTrusted(X509Certificate[] cert)
    {
        return true;
    }

    public boolean isServerTrusted(X509Certificate[] cert)
    {
        try
        {
            cert[0].checkValidity();
            return true;
        }
        catch (CertificateExpiredException e)
        {
            return false;
        }
        catch (CertificateNotYetValidException e)
        {
            return false;
        }
    }

    public void checkClientTrusted(X509Certificate[] x509Certificates, String s)
        throws CertificateException
    {
        // Do nothing for now.
    }

    public void checkServerTrusted(X509Certificate[] x509Certificates, String s)
        throws CertificateException
    {
        // Do nothing for now.
    }

    public X509Certificate[] getAcceptedIssuers()
    {
        return new X509Certificate[0];
    }
}
