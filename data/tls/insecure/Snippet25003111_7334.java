/**
* 
*/
package toto;

import javax.net.ssl.*;

import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.*;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class JerseyClient {

private WebTarget webTarget;
private Client client;
private ClientConfig clientConfig;
//private static final String BASE_URI = "https://localhost:8443/testsecurity2/resources";
private static final String truststore_path = "/var/toto/toti/security/truststore.jks";
private static final String truststore_password = "changeit";
private static final String keystore_path = "/var/toto/toti/security/keystore.jks";
private static final String keystore_password = "changeit";
private static final String url = "https://localhost/restapi/";

public JerseyClient() {
    ClientConfig config = new ClientConfig(); // SSL configuration
    // SSL configuration

    // new Jersey 2.x way to do it
    client = ClientBuilder.newBuilder()
            .sslContext(getSSLContext())
            .hostnameVerifier(getHostnameVerifier())
            .build();
    webTarget = client.target(url);

}

public JerseyClient(String URL) {
    // SSL configuration
    client = ClientBuilder.newBuilder()
            .sslContext(getSSLContext())
            .hostnameVerifier(getHostnameVerifier())
            .build();
    webTarget = client.target(URL);
}

public <T> T get_XML(Class<T> responseType) {
    return webTarget.request(MediaType.APPLICATION_JSON).get(responseType);
}

public <T> T get_JSON(Class<T> responseType) {
    return webTarget.request(MediaType.APPLICATION_JSON).get(responseType);
}

public Response get_JSON() {
    return webTarget.request(MediaType.APPLICATION_JSON).get();
}

public void close() {
    client.close();
}

public void setUsernamePassword(String username, String password) {
    HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
    client.register(feature);
}

private HostnameVerifier getHostnameVerifier() {
    return new HostnameVerifier() {

        @Override
        public boolean verify(String hostname, javax.net.ssl.SSLSession sslSession) {
            return true;
        }
    };
}

private SSLContext getSSLContext() {

    TrustManager mytm[] = null;
    KeyManager mykm[] = null;

    try {
        mytm = new TrustManager[]{new MyX509TrustManager(truststore_path, truststore_password.toCharArray())};
        mykm = new KeyManager[]{new MyX509KeyManager(keystore_path, keystore_password.toCharArray())};
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    SSLContext ctx = null;
    try {
        ctx = SSLContext.getInstance("SSL");
        ctx.init(mykm, mytm, null);
    } catch (java.security.GeneralSecurityException ex) {
    }
    return ctx;
}

/**
 * Taken from http://java.sun.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html
 *
 */
static class MyX509TrustManager implements X509TrustManager {

    /*
     * The default PKIX X509TrustManager9.  We'll delegate
     * decisions to it, and fall back to the logic in this class if the
     * default X509TrustManager doesn't trust it.
     */
    X509TrustManager pkixTrustManager;

    MyX509TrustManager(String trustStore, char[] password) throws Exception {
        this(new File(trustStore), password);
    }

    MyX509TrustManager(File trustStore, char[] password) throws Exception {
        // create a "default" JSSE X509TrustManager.

        KeyStore ks = KeyStore.getInstance("JKS");

        ks.load(new FileInputStream(trustStore), password);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
        tmf.init(ks);

        TrustManager tms[] = tmf.getTrustManagers();

        /*
         * Iterate over the returned trustmanagers, look
         * for an instance of X509TrustManager.  If found,
         * use that as our "default" trust manager.
         */
        for (int i = 0; i < tms.length; i++) {
            if (tms[i] instanceof X509TrustManager) {
                pkixTrustManager = (X509TrustManager) tms[i];
                return;
            }
        }

        /*
         * Find some other way to initialize, or else we have to fail the
         * constructor.
         */
        throw new Exception("Couldn't initialize");
    }

    /*
     * Delegate to the default trust manager.
     */
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
        try {
            pkixTrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException excep) {
            /*
             * Possibly pop up a dialog box asking whether to trust the
             * cert chain.
             */
        }
    }

    /*
     * Merely pass this through.
     */
    public X509Certificate[] getAcceptedIssuers() {
        return pkixTrustManager.getAcceptedIssuers();
    }

    @Override
    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
            throws CertificateException {
        // TODO Auto-generated method stub

    }
} // end class MyX509TrustManager

/**
 * Inspired from http://java.sun.com/javase/6/docs/technotes/guides/security/jsse/JSSERefGuide.html
 *
 */
static class MyX509KeyManager implements X509KeyManager {

    /*
     * The default PKIX X509KeyManager.  We'll delegate
     * decisions to it, and fall back to the logic in this class if the
     * default X509KeyManager doesn't trust it.
     */
    X509KeyManager pkixKeyManager;

    MyX509KeyManager(String keyStore, char[] password) throws Exception {
        this(new File(keyStore), password);
    }

    MyX509KeyManager(File keyStore, char[] password) throws Exception {
        // create a "default" JSSE X509KeyManager.

        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new FileInputStream(keyStore), password);

        KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509", "SunJSSE");
        kmf.init(ks, password);

        KeyManager kms[] = kmf.getKeyManagers();

        /*
         * Iterate over the returned keymanagers, look
         * for an instance of X509KeyManager.  If found,
         * use that as our "default" key manager.
         */
        for (int i = 0; i < kms.length; i++) {
            if (kms[i] instanceof X509KeyManager) {
                pkixKeyManager = (X509KeyManager) kms[i];
                return;
            }
        }

        /*
         * Find some other way to initialize, or else we have to fail the
         * constructor.
         */
        throw new Exception("Couldn't initialize");
    }

    public PrivateKey getPrivateKey(String arg0) {
        return pkixKeyManager.getPrivateKey(arg0);
    }

    public X509Certificate[] getCertificateChain(String arg0) {
        return pkixKeyManager.getCertificateChain(arg0);
    }

    public String[] getClientAliases(String arg0, Principal[] arg1) {
        return pkixKeyManager.getClientAliases(arg0, arg1);
    }

    public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
        return pkixKeyManager.chooseClientAlias(arg0, arg1, arg2);
    }

    public String[] getServerAliases(String arg0, Principal[] arg1) {
        return pkixKeyManager.getServerAliases(arg0, arg1);
    }

    public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
        return pkixKeyManager.chooseServerAlias(arg0, arg1, arg2);
    }
} // end class MyX509KeyManager


} // end class JerseyClient
