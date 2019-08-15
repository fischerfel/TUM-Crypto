package fr.csf.ssl;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import javax.net.ssl.X509TrustManager;

/**
 * A factory class which creates an {@link SSLContext} that
 * naively accepts all certificates without verification.
 */
public class NaiveSSLContext
{
    private NaiveSSLContext()
    {}

    /**
     * Get an SSLContext that implements the specified secure
     * socket protocol and naively accepts all certificates
     * without verification.
     */
    public static SSLContext getInstance( String protocol) throws NoSuchAlgorithmException
    {
        SSLContext sslCtx = SSLContext.getInstance( protocol);
        init( sslCtx);
        return sslCtx;
    }

    /**
     * Get an SSLContext that implements the specified secure
     * socket protocol and naively accepts all certificates
     * without verification.
     */
    public static SSLContext getInstance( String protocol, Provider provider) throws NoSuchAlgorithmException
    {
        SSLContext sslCtx = SSLContext.getInstance( protocol, provider);
        init( sslCtx);
        return sslCtx;
    }

    /**
     * Get an SSLContext that implements the specified secure
     * socket protocol and naively accepts all certificates
     * without verification.
     */
    public static SSLContext getInstance( String protocol, String provider) throws NoSuchAlgorithmException, NoSuchProviderException
    {
        SSLContext sslCtx = SSLContext.getInstance( protocol, provider);
        init( sslCtx);
        return sslCtx;
    }

    /**
     * Set NaiveTrustManager to the given context.
     */
    private static void init( SSLContext context)
    {
        try
        {
            // Set NaiveTrustManager.
            context.init( null, new TrustManager[] { new NaiveTrustManager() }, new java.security.SecureRandom());
            System.out.println( "------------- Initialisation du NaiveSSLContext ---------------------");
        }
        catch( java.security.KeyManagementException e)
        {
            throw new RuntimeException( "Failed to initialize an SSLContext.", e);
        }
    }

    /**
     * A {@link TrustManager} which trusts all certificates naively.
     */
    private static class NaiveTrustManager implements X509TrustManager
    {
        @Override
        public X509Certificate[] getAcceptedIssuers()
        {
            System.out.println( "------------- NaiveTrustManager.getAcceptedIssuers() ---------------------");
            return null;
        }

        @Override
        public void checkClientTrusted( X509Certificate[] certs, String authType)
        {
            System.out.println( "------------- NaiveTrustManager.checkClientTrusted( " + certs.toString() + ", " + authType
                    + ") ---------------------");
        }

        @Override
        public void checkServerTrusted( X509Certificate[] certs, String authType)
        {
            System.out.println( "------------- NaiveTrustManager.checkServerTrusted( " + certs.toString() + ", " + authType
                    + ") ---------------------");
        }
    }
}
