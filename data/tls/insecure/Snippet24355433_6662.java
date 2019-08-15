import org.spongycastle.jce.X509Principal;
import org.spongycastle.jce.provider.BouncyCastleProvider;
import org.spongycastle.x509.X509V3CertificateGenerator;

import javax.net.ssl.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

public class HttpsHello {
    private static String domainName = "localhost";
    static { Security.addProvider(new BouncyCastleProvider());  }

    public static void test() {

        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(1024);
            KeyPair KPair = keyPairGenerator.generateKeyPair();

            X509V3CertificateGenerator v3CertGen = new X509V3CertificateGenerator();

             int ran = new SecureRandom().nextInt();
            if (ran < 0) ran = ran *-1;

            BigInteger serialNumber = BigInteger.valueOf(ran);

            v3CertGen.setSerialNumber(serialNumber);
            v3CertGen.setIssuerDN(new X509Principal("CN=" + domainName + ", OU=None, O=None L=None, C=None"));
            v3CertGen.setNotBefore(new Date(System.currentTimeMillis() - 1000L * 60 * 60 * 24 * 30));
            v3CertGen.setNotAfter(new Date(System.currentTimeMillis() + (1000L * 60 * 60 * 24 * 365*10)));
            v3CertGen.setSubjectDN(new X509Principal("CN=" + domainName + ", OU=None, O=None L=None, C=None"));


            v3CertGen.setPublicKey(KPair.getPublic());
            v3CertGen.setSignatureAlgorithm("MD5WithRSAEncryption");

            X509Certificate pkcert = v3CertGen.generateX509Certificate(KPair.getPrivate());
        //    FileOutputStream fos = new FileOutputStream("/path/to/testCert.cert");
          //  fos.write(pkcert.getEncoded());
           // fos.close();

            ByteArrayInputStream cert = new ByteArrayInputStream(pkcert.getEncoded());

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());

            ks.load(cert,null);
            KeyManagerFactory kmf =
                    KeyManagerFactory.getInstance("SunX509");
            kmf.init(ks, null);
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(kmf.getKeyManagers(), null, null);
            SSLServerSocketFactory ssf = sc.getServerSocketFactory();
            SSLServerSocket s
                    = (SSLServerSocket) ssf.createServerSocket(8888);
            System.out.println("Server started:");
            printServerSocketInfo(s);
            // Listening to the port
            SSLSocket c = (SSLSocket) s.accept();
            printSocketInfo(c);
            BufferedWriter w = new BufferedWriter(
                    new OutputStreamWriter(c.getOutputStream()));
            BufferedReader r = new BufferedReader(
                    new InputStreamReader(c.getInputStream()));
            String m = r.readLine();
            w.write("HTTP/1.0 200 OK");
            w.newLine();
            w.write("Content-Type: text/html");
            w.newLine();
            w.newLine();
            w.write("<html><body>Hello world!</body></html>");
            w.newLine();
            w.flush();
            w.close();
            r.close();
            c.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void printSocketInfo(SSLSocket s) {
        System.out.println("Socket class: "+s.getClass());
        System.out.println("   Remote address = "
                +s.getInetAddress().toString());
        System.out.println("   Remote port = "+s.getPort());
        System.out.println("   Local socket address = "
                +s.getLocalSocketAddress().toString());
        System.out.println("   Local address = "
                +s.getLocalAddress().toString());
        System.out.println("   Local port = "+s.getLocalPort());
        System.out.println("   Need client authentication = "
                +s.getNeedClientAuth());
        SSLSession ss = s.getSession();
        System.out.println("   Cipher suite = "+ss.getCipherSuite());
        System.out.println("   Protocol = "+ss.getProtocol());
    }
    private static void printServerSocketInfo(SSLServerSocket s) {
        System.out.println("Server socket class: "+s.getClass());
        System.out.println("   Socker address = "
                +s.getInetAddress().toString());
        System.out.println("   Socker port = "
                +s.getLocalPort());
        System.out.println("   Need client authentication = "
                +s.getNeedClientAuth());
        System.out.println("   Want client authentication = "
                +s.getWantClientAuth());
        System.out.println("   Use client mode = "
                +s.getUseClientMode());
    }
}
