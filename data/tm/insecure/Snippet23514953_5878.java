import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.*;

private TrustManager[] getBypassingTrustManager()
{
    TrustManager[] certs = new TrustManager[]
        {
            new X509TrustManager()
            {
                public X509Certificate[] getAcceptedIssuers()
                {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs, String t)
                {
                }

                public void checkServerTrusted(X509Certificate[] certs, String t)
                {
                }
            }
        };
    return certs;
}


SSLContext sslCtx = SSLContext.getInstance("SSL");
TrustManager trustManager[] = getBypassingTrustManager();
sslCtx.init(null, trustManager, new SecureRandom());
HttpsURLConnection.setDefaultSSLSocketFactory(sslCtx.getSocketFactory());
