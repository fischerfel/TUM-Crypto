package utils.ssl;

import android.content.Context;
import android.util.Log;

import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

 import javax.net.ssl.HostnameVerifier;
javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import utils.global.AppUtils;
import webServices.global.RequestStringBuilder;


public class PinnedCertificateHttpsURLConnectionFactory
{
private final static String TAG = PinnedCertificateHttpsURLConnectionFactory.class.getSimpleName();

private final Context mContext;

public PinnedCertificateHttpsURLConnectionFactory(Context iContext)
{
    mContext = iContext;
}

HttpsURLConnection createHttpsURLConnection(String urlString, final String iTrustStoreAssetName, final String iTrustStorePassword) throws Throwable
{
    // Initialize the trust manager factory instance with our trust store
    // as source of certificate authorities and trust material.
    KeyStore trustStore = new TrustStoreFactory(iTrustStoreAssetName, iTrustStorePassword, mContext).createTrustStore();
    String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
    TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(tmfAlgorithm);
    trustManagerFactory.init(trustStore);

    // Initialize the SSL context.
    TrustManager[] wrappedTrustManagers = getWrappedTrustManagers(trustManagerFactory.getTrustManagers());
    SSLContext sslContext = SSLContext.getInstance(SslUtils.PROTOCOL_TLS);
    sslContext.init(null, wrappedTrustManagers, null);

    // Create the https URL connection.
    URL url = new URL(urlString);
    HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
    urlConnection.setSSLSocketFactory(sslContext.getSocketFactory());
    urlConnection.setHostnameVerifier(getHostnameVerifier());

    return urlConnection;
}

// Let's assume your server app is hosting inside a server machine
// which has a server certificate in which "Issued to" is "localhost",for example.
// Then, inside verify method you can verify "localhost".
// If not, you can temporarily return true
private HostnameVerifier getHostnameVerifier()
{
    return new HostnameVerifier()
    {
        @Override
        public boolean verify(String hostname, SSLSession session)
        {
            HostnameVerifier hv = HttpsURLConnection.getDefaultHostnameVerifier();

            String localHost = SslUtils.SSL_LOCAL_HOST_DEV;

            if (RequestStringBuilder.isEnvironmentProd())
            {
                localHost = SslUtils.SSL_LOCAL_HOST_PROD;
            }

            return hv.verify(localHost, session);
            //              return hv.verify("localhost", session);
            //              return true;
        }
    };
}

private TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers)
{
    final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];

    final X509TrustManager x509TrustManager = new X509TrustManager()
    {
        public X509Certificate[] getAcceptedIssuers()
        {
            return originalTrustManager.getAcceptedIssuers();
        }

        public void checkClientTrusted(X509Certificate[] certs, String authType)
        {
            try
            {
                if (certs != null && certs.length > 0)
                {
                    for (X509Certificate cer : certs)
                    {
                        cer.checkValidity();
                    }
                }
                else
                {
                    originalTrustManager.checkClientTrusted(certs, authType);
                }
            }
            catch (CertificateException e)
            {
                AppUtils.printLog(Log.ERROR, TAG, "checkClientTrusted" + e.toString());
            }
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType)
        {
            try
            {
                if (certs != null && certs.length > 0)
                {
                    for (X509Certificate cer : certs)
                    {
                        cer.checkValidity();
                    }
                }
                else
                {
                    originalTrustManager.checkServerTrusted(certs, authType);
                }
            }
            catch (CertificateException e)
            {
                AppUtils.printLog(Log.ERROR, TAG, "checkServerTrusted" + e.toString());
            }
        }
    };

    return new TrustManager[] {x509TrustManager};
}
}
