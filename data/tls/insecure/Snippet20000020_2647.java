import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManagerFactory;

public class ListCiphers
{
    public static void main(String[] args)
    throws Exception
    {
        SSLContext ctx = SSLContext.getInstance("TLSv1");
        // Create an empty TrustManagerFactory to avoid loading default CA
        KeyStore ks = KeyStore.getInstance("JKS");
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(ks);
        ctx.init(null, tmf.getTrustManagers(), null);
        SSLSocket socket = (SSLSocket) ctx.getSocketFactory().createSocket("mozilla.org", 443);
        printSupportedCiphers(socket);
        printEnabledCiphers(socket);
    }

    private static void printSupportedCiphers(SSLSocket socket)
    {
        printInfos("Supported cipher suites", socket.getSupportedCipherSuites());
    }

    private static void printEnabledCiphers(SSLSocket socket)
    {
        printInfos("Enabled cipher suites", socket.getEnabledCipherSuites());
    }

    private static void printInfos(String prefix, String[] values)
    {
        System.out.println(prefix + ":");
        for (int i = 0; i < values.length; i++)
            System.out.println("  " + values[i]);
    }
}
