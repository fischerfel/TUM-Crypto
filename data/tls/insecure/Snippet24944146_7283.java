import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.net.SocketFactory;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509KeyManager;

import org.apache.commons.httpclient.ConnectTimeoutException;
import org.apache.commons.httpclient.params.HttpConnectionParams;
import org.apache.commons.httpclient.protocol.ProtocolSocketFactory;

import br.com.ps.transp.util.Const;

public class HSProtocolSocketFactory implements ProtocolSocketFactory {

    private static final String TRUSTSTORE = System.getProperty("javax.net.ssl.trustStore");
    private SSLContext ssl = null;
    private X509Certificate certificate;
    private PrivateKey privateKey;

    public HSProtocolSocketFactory(X509Certificate certificate, PrivateKey privateKey) {
        this.certificate = certificate;
        this.privateKey = privateKey;
    }

    private SSLContext createSSLContext() {
        try {
            KeyManager[] keyManagers = createKeyManagers();
            TrustManager[] trustManagers = createTrustManagers();
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagers, trustManagers, null);

            return sslContext;
        } catch (KeyManagementException e) {
            Logger.getLogger(HSProtocolSocketFactory.class.getName()).log(Level.SEVERE, null, e);
        } catch (KeyStoreException e) {
            Logger.getLogger(HSProtocolSocketFactory.class.getName()).log(Level.SEVERE, null, e);
        } catch (NoSuchAlgorithmException e) {
            Logger.getLogger(HSProtocolSocketFactory.class.getName()).log(Level.SEVERE, null, e);
        } catch (CertificateException e) {
            Logger.getLogger(HSProtocolSocketFactory.class.getName()).log(Level.SEVERE, null, e);
        } catch (IOException e) {
            Logger.getLogger(HSProtocolSocketFactory.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    private SSLContext getSSLContext() {
        if (ssl == null) {
            ssl = createSSLContext();
        }
        return ssl;
    }

    public Socket createSocket(String host, int port, InetAddress localAddress, int localPort, HttpConnectionParams params)
            throws IOException, UnknownHostException, ConnectTimeoutException {
        if (params == null) {
            throw new IllegalArgumentException("Parameters may not be null");
        }
        int timeout = params.getConnectionTimeout();
        SocketFactory socketfactory = getSSLContext().getSocketFactory();
        if (timeout == 0) {
            return socketfactory.createSocket(host, port, localAddress, localPort);
        }

        Socket socket = socketfactory.createSocket();
        SocketAddress localaddr = new InetSocketAddress(localAddress, localPort);
        SocketAddress remoteaddr = new InetSocketAddress(host, port);
        socket.bind(localaddr);
        try {
            socket.connect(remoteaddr, timeout);
        } catch (Throwable t) {
            Logger.getLogger(HSProtocolSocketFactory.class.getName()).log(Level.SEVERE, null, t);
            throw new ConnectTimeoutException("Connection timeout error", t);
        }

        return socket;
    }

    public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port, clientHost, clientPort);
    }

    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(host, port);
    }

    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return getSSLContext().getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    public KeyManager[] createKeyManagers() {
        HSKeyManager keyManager = new HSKeyManager(certificate, privateKey);

        return new KeyManager[] { keyManager };
    }

    public TrustManager[] createTrustManagers() throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
        KeyStore trustStore = KeyStore.getInstance("JKS");
        char[] senhaTrustStore = Const.SENHA_CACERTS.toCharArray();
        InputStream inputCacerts = null;

        inputCacerts = new FileInputStream(TRUSTSTORE);
        trustStore.load(inputCacerts, senhaTrustStore);
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(trustStore);
        return trustManagerFactory.getTrustManagers();
    }

    class HSKeyManager implements X509KeyManager {

        private X509Certificate certificate;
        private PrivateKey privateKey;

        public HSKeyManager(X509Certificate certificate, PrivateKey privateKey) {
            this.certificate = certificate;
            this.privateKey = privateKey;
        }

        public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
            return certificate.getIssuerDN().getName();
        }

        public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
            return null;
        }

        public X509Certificate[] getCertificateChain(String arg0) {
            return new X509Certificate[] { certificate };
        }

        public String[] getClientAliases(String arg0, Principal[] arg1) {
            return new String[] { certificate.getIssuerDN().getName() };
        }

        public PrivateKey getPrivateKey(String arg0) {
            return privateKey;
        }

        public String[] getServerAliases(String arg0, Principal[] arg1) {
            return null;
        }
    }
}
