import java.io.IOException;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class GetCertificates {

public static void main(String[] args) throws NoSuchAlgorithmException,    KeyManagementException, IOException{
    String host="google.com";
    int port = 443;
    SSLContext ssl = SSLContext.getInstance("TLS");
    ssl.init(null, new TrustManager[]{new SimpleX509TrustManager()}, null);
    SSLSocketFactory factory = ssl.getSocketFactory();
    SSLSocket socket = (SSLSocket) factory.createSocket(host,port);
    SSLSession session = socket.getSession();
    javax.security.cert.X509Certificate[] certs = session.getPeerCertificateChain();
    System.out.println(certs[certs.length-1].getSubjectDN());
    // you can display certificates info here and also cipher suites
    session.getCipherSuite();
    session.invalidate();
}
}
class SimpleX509TrustManager implements X509TrustManager {
public void checkClientTrusted(
        X509Certificate[] cert, String a)
        throws CertificateException {
}

public void checkServerTrusted(
        X509Certificate[] cert, String a)
        throws CertificateException {
}

public X509Certificate[] getAcceptedIssuers() {
    return new X509Certificate[0];
}
}
