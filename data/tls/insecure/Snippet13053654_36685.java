import java.io.*;
import java.nio.file.*;
import java.security.*;
import java.security.cert.*;
import java.security.interfaces.*;
import javax.net.ssl.*;

import org.bouncycastle.jce.provider.*;
import org.bouncycastle.openssl.*;

static SSLSocketFactory getSocketFactory (final String caCrtFile, final String crtFile, final String keyFile, final String password) throws Exception
{ 
    Security.addProvider(new BouncyCastleProvider());

    // load CA certificate
    PEMReader reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(caCrtFile)))));
    X509Certificate caCert = (X509Certificate)reader.readObject();
    reader.close();

    // load client certificate
    reader = new PEMReader(new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(crtFile)))));
    X509Certificate cert = (X509Certificate)reader.readObject();
    reader.close();

    // load client private key
    reader = new PEMReader(
            new InputStreamReader(new ByteArrayInputStream(Files.readAllBytes(Paths.get(keyFile)))),
            new PasswordFinder() {
                public char[] getPassword() {
                    return password.toCharArray();
                }
            }
    );
    KeyPair key = (KeyPair)reader.readObject();
    reader.close();

    // CA certificate is used to authenticate server
    KeyStore caKs = KeyStore.getInstance("JKS");
    caKs.load(null, null);
    caKs.setCertificateEntry("ca-certificate", caCert);
    TrustManagerFactory tmf = TrustManagerFactory.getInstance("PKIX");
    tmf.init(caKs);

    // client key and certificates are sent to server so it can authenticate us
    KeyStore ks = KeyStore.getInstance("JKS");
    ks.load(null, null);
    ks.setCertificateEntry("certificate", cert);
    ks.setKeyEntry("private-key", key.getPrivate(), password.toCharArray(), new java.security.cert.Certificate[]{cert});
    KeyManagerFactory kmf = KeyManagerFactory.getInstance("PKIX");
    kmf.init(ks, password.toCharArray());

    // finally, create SSL socket factory
    SSLContext context = SSLContext.getInstance("TLSv1");
    context.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

    return context.getSocketFactory();
}
