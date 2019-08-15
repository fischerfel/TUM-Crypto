import java.net.Socket;
import java.security.KeyStore;
import java.security.Provider;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.ManagerFactoryParameters;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactorySpi;
import javax.net.ssl.X509ExtendedTrustManager;

public class MyProvider extends Provider {
    public MyProvider() {
        super("MyProvider", 1.0, "Trust certificates");
        put("TrustManagerFactory.TrustAllCertificates", MyTrustManagerFactory.class.getName());
    }

    public static class MyTrustManagerFactory extends TrustManagerFactorySpi {
        public MyTrustManagerFactory() {}
        protected void engineInit( KeyStore keystore ) {}
        protected void engineInit(ManagerFactoryParameters mgrparams ) {}
        protected TrustManager[] engineGetTrustManagers() {
            return new TrustManager[] {
                new X509ExtendedTrustManager() {

                    @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {}

                @Override                           
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s, Socket socket) throws CertificateException {}

                @Override
                public void checkClientTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {}

                @Override
                public void checkServerTrusted(X509Certificate[] x509Certificates, String s, SSLEngine sslEngine) throws CertificateException {}
                }                                       
            };
        }
    }
    }
