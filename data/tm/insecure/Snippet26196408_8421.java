package testing;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLClientTest {
    public static void main(String[] args) {
        int port = 443;
        String host = "google.com";

        try {

            SSLContext sc = SSLContext.getInstance("TLSv1.2");
            KeyStore ks = KeyStore.getInstance("JKS");
            InputStream ksIs = new FileInputStream("securecert.certificate");
            try {
                ks.load(ksIs, "pwdpwdpwd".toCharArray());
            } finally {
                if (ksIs != null) {
                    ksIs.close();
                }
            }
            KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, "pwdpwdpwd".toCharArray());
            sc.init(kmf.getKeyManagers(),
                    new TrustManager[] { new MyTrustManager() }, null);

            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory
                    .getDefault();
            SSLSocket socket = (SSLSocket) factory.createSocket(host, port);
            socket.startHandshake();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    socket.getInputStream()));
            System.out.println(in.readLine());
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static final class MyTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                throws CertificateException {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[] {};
        }
    }
}
