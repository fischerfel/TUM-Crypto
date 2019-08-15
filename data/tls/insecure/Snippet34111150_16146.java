import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLHandshakeException;
import javax.net.ssl.TrustManagerFactory;

public class SSLExample {
    // BEGIN ------- ADDME
    static {
        try {
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            Path ksPath = Paths.get(System.getProperty("java.home"),
                    "lib", "security", "cacerts");
            keyStore.load(Files.newInputStream(ksPath),
                    "changeit".toCharArray());

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            try (InputStream caInput = new BufferedInputStream(
                    // this files is shipped with the application
                    SSLExample.class.getResourceAsStream("DSTRootCAX3.der"))) {
                Certificate crt = cf.generateCertificate(caInput);
                System.out.println("Added Cert for " + ((X509Certificate) crt)
                        .getSubjectDN());

                keyStore.setCertificateEntry("DSTRootCAX3", crt);
            }

            if (false) { // enable to see
                System.out.println("Truststore now trusting: ");
                PKIXParameters params = new PKIXParameters(keyStore);
                params.getTrustAnchors().stream()
                        .map(TrustAnchor::getTrustedCert)
                        .map(X509Certificate::getSubjectDN)
                        .forEach(System.out::println);
                System.out.println();
            }

            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(keyStore);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, tmf.getTrustManagers(), null);
            SSLContext.setDefault(sslContext);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    // END ---------- ADDME

    public static void main(String[] args) throws IOException {
        // signed by default trusted CAs.
        testUrl(new URL("https://google.com"));
        testUrl(new URL("https://www.thawte.com"));

        // signed by letsencrypt
        testUrl(new URL("https://helloworld.letsencrypt.org"));
        // signed by LE's cross-sign CA
        testUrl(new URL("https://letsencrypt.org"));
        // expired
        testUrl(new URL("https://tv.eurosport.com/"));
        // self-signed
        testUrl(new URL("https://www.pcwebshop.co.uk/"));

    }

    static void testUrl(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        try {
            connection.connect();
            System.out.println("Headers of " + url + " => "
                    + connection.getHeaderFields());
        } catch (SSLHandshakeException e) {
            System.out.println("Untrusted: " + url);
        }
    }

}
