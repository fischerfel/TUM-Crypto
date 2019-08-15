import javax.net.ssl.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class CustomTrustManager implements X509TrustManager
{
    private static final String JAVA_CA_CERT_FILE_NAME = "cacerts";
    private static final String CLASSIC_JAVA_CA_CERT_FILE_NAME = "jssecacerts";
    private static final int DEFAULT_HTTPS_PORT = 443;

    private String[] hostsToTrust = {"server1.company.com", "server2.company.com"};
    private char[] defaultCAKeystorePassphrase = "changeit".toCharArray();
    private KeyStore certificateTrustStore;
    private X509TrustManager defaultTrustManager;

    public static void initSsl()
    {
        try
        {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new TrustManager[] { new CustomTrustManager() }, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public CustomTrustManager()
    {
        try
        {
            initTrustStore();
            addTrustedHosts();
            initDefaultTrustManager();
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
        defaultTrustManager.checkClientTrusted(chain, authType);
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException
    {
        defaultTrustManager.checkServerTrusted(chain, authType);
    }

    public X509Certificate[] getAcceptedIssuers()
    {
        return defaultTrustManager.getAcceptedIssuers();
    }

    private void initTrustStore() throws Exception
    {
        File javaTrustStoreFile = findJavaTrustStoreFile();
        InputStream inputStream = new FileInputStream(javaTrustStoreFile);
        certificateTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        certificateTrustStore.load(inputStream, defaultCAKeystorePassphrase);
        inputStream.close();
    }

    private void addTrustedHosts() throws Exception
    {
        SSLContext tempConnectContext = SSLContext.getInstance("TLS");
        ExtractX509CertTrustManager getX509CertTrustManager = new ExtractX509CertTrustManager();
        tempConnectContext.init(null, new TrustManager[] { getX509CertTrustManager }, null);
        SSLSocketFactory socketFactory = tempConnectContext.getSocketFactory();
        for (String host : hostsToTrust)
        {
            SSLSocket socket = (SSLSocket) socketFactory.createSocket(host, DEFAULT_HTTPS_PORT);
            // connect the socket to set the cert chain in getX509CertTrustManager
            socket.startHandshake();
            for (X509Certificate cert : getX509CertTrustManager.getCurrentChain())
            {
                if (!certificateTrustStore.isCertificateEntry(host))
                {
                    certificateTrustStore.setCertificateEntry(host, cert);
                }
            }
        }
    }

    private void initDefaultTrustManager() throws Exception
    {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(certificateTrustStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
        for (TrustManager trustManager : trustManagers)
        {
            if (trustManager instanceof X509TrustManager)
            {
                defaultTrustManager = (X509TrustManager) trustManager;
                break;
            }
        }
    }

    /**
     * Trust Manager for the sole purpose of retrieving the X509 cert when a connection is made to a host we want
     * to start trusting.
     */
    private static class ExtractX509CertTrustManager implements X509TrustManager
    {
        private X509Certificate[] currentChain;
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException { }
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException
        {
            currentChain = x509Certificates;
        }
        public X509Certificate[] getAcceptedIssuers() { return null; }
        public X509Certificate[] getCurrentChain()
        {
            return currentChain;
        }
    }

    private File findJavaTrustStoreFile()
    {
        File javaHome = new File(System.getProperty("java.home") + File.separatorChar + "lib" + File.separatorChar + "security");
        File caCertsFile = new File(javaHome, JAVA_CA_CERT_FILE_NAME);
        if (!caCertsFile.exists() || !caCertsFile.isFile())
        {
            caCertsFile = new File(javaHome, CLASSIC_JAVA_CA_CERT_FILE_NAME);
        }
        return caCertsFile;
    }
} 
