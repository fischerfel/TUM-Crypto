import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class CertManager {

private static final char[] passphrase = "changeit".toCharArray();
private String rootPath;
private SSLSocketFactory factory;
private SavingTrustManager trustManager;
private KeyStore keyStore;


/**
 * Creates a CertManager.
 * @param rootPath Path to directory where the file 'jssecacerts' is located.
 */
public CertManager(String rootPath){
    this.rootPath = rootPath;       
}

/**
 * Gets a SSLSocketFactory with the trusted certs.
 * @return
 * @throws Exception
 */
public SSLSocketFactory getSSLSocketFactory() throws Exception {
    //Load trusted certs
    File file = new File(rootPath+"jssecacerts");

    System.out.println("Loading KeyStore " + file + "...");
    InputStream in = new FileInputStream(file);
    keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
    keyStore.load(in, passphrase);
    in.close();

    //Use these certs 
    SSLContext context = SSLContext.getInstance("TLS");
    TrustManagerFactory tmf =
        TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
    tmf.init(keyStore);
    X509TrustManager defaultTrustManager = (X509TrustManager)tmf.getTrustManagers()[0];
    trustManager = new SavingTrustManager(defaultTrustManager);
    context.init(null, new TrustManager[] {trustManager}, null);
    factory = context.getSocketFactory();
    return factory;
}

private static class SavingTrustManager implements X509TrustManager {

    private final X509TrustManager tm;
    private X509Certificate[] chain;

    SavingTrustManager(X509TrustManager tm) {
        this.tm = tm;
    }

    public X509Certificate[] getAcceptedIssuers() {         
        return tm.getAcceptedIssuers();
    }

    public void checkClientTrusted(X509Certificate[] chain, String authType)
    throws CertificateException {
        this.chain = chain;
        tm.checkClientTrusted(chain, authType);
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType)
    throws CertificateException {
        this.chain = chain;
        tm.checkServerTrusted(chain, authType);
    }
}
}
