// import com.sun.xml.ws.developer.JAXWSProperties;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.ws.BindingProvider;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

/**
 *
 * Usage examples (BindingProvider port):
 * NaiveSSLHelper.makeWebServiceClientTrustEveryone(port); // GlassFish
 * NaiveSSLHelper.makeCxfWebServiceClientTrustEveryone(port); // TomEE
 * 
 * Based on Erik Wramner's example frome here:
 * http://erikwramner.wordpress.com/2013/03/27/trust-self-signed-ssl-certificates-and-skip-host-name-verification-with-jax-ws/
 *
 * I have extended the functionality when Apache CXF is used.
 */
public class NaiveSSLHelper {

    private static final String JAXWS_HOSTNAME_VERIFIER = "com.sun.xml.ws.transport.https.client.hostname.verifier"; // JAXWSProperties.HOSTNAME_VERIFIER;
    private static final String JAXWS_SSL_SOCKET_FACTORY = "com.sun.xml.ws.transport.https.client.SSLSocketFactory"; // JAXWSProperties.SSL_SOCKET_FACTORY;

    // In Glassfish (Metro) environment you can use this function (Erik Wramner's solution)
    public static void makeWebServiceClientTrustEveryone(Object webServicePort) {
        if (webServicePort instanceof BindingProvider) {
            BindingProvider bp = (BindingProvider) webServicePort;
            Map requestContext = bp.getRequestContext();
            requestContext.put(JAXWS_SSL_SOCKET_FACTORY, getTrustingSSLSocketFactory());
            requestContext.put(JAXWS_HOSTNAME_VERIFIER, new NaiveHostnameVerifier());
        } else {
            throw new IllegalArgumentException(
                    "Web service port "
                    + webServicePort.getClass().getName()
                    + " does not implement "
                    + BindingProvider.class.getName());
        }
    }

    // In TomEE (Apache CXF) environment you can use this function (my solution)
    public static void makeCxfWebServiceClientTrustEveryone(Object port) {
        TrustManager[] trustManagers = new TrustManager[]{
            new NaiveTrustManager()
        };
        Client c = ClientProxy.getClient(port);
        HTTPConduit httpConduit = (HTTPConduit) c.getConduit();
        TLSClientParameters tlsParams = new TLSClientParameters();
        tlsParams.setSecureSocketProtocol("SSL");
        tlsParams.setKeyManagers(new KeyManager[0]);
        tlsParams.setTrustManagers(trustManagers);
        tlsParams.setDisableCNCheck(true);
        httpConduit.setTlsClientParameters(tlsParams);
    }

    public static SSLSocketFactory getTrustingSSLSocketFactory() {
        return SSLSocketFactoryHolder.INSTANCE;
    }

    private static SSLSocketFactory createSSLSocketFactory() {
        TrustManager[] trustManagers = new TrustManager[]{
            new NaiveTrustManager()
        };
        SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(new KeyManager[0], trustManagers, new SecureRandom());
            return sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            return null;
        }
    }

    private static interface SSLSocketFactoryHolder {

        public static final SSLSocketFactory INSTANCE = createSSLSocketFactory();
    }

    private static class NaiveHostnameVerifier implements
            HostnameVerifier {

        @Override
        public boolean verify(String hostName,
                SSLSession session) {
            return true;
        }
    }

    private static class NaiveTrustManager implements
            X509TrustManager {

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] certs,
                String authType) throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] certs,
                String authType) throws java.security.cert.CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }
}
