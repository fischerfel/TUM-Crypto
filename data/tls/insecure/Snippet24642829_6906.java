import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.security.cert.CertPath;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsNetClient {
    public static void main(String args[]) throws Exception {
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, // No Key Manager
            new TrustManager[] { new X509TrustManager()
              {
                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                    throws CertificateException
                  {}

                @Override
                public void checkServerTrusted(X509Certificate[] xcert, String arg1)
                    throws CertificateException {
                    System.out.println("Certificate");
                  /*  for( X509Certificate certificate : xcert)
                    {
                        System.out.println(certificate.toString());
                    }*/
                     // check the certs
                }

                @Override
                public X509Certificate[] getAcceptedIssuers()
                  {
                    return null;
                  }

              } }, // TrustManager 
            null);
        SSLSocketFactory factory = context.getSocketFactory();
        SSLSocket socket = (SSLSocket) factory.createSocket("127.0.0.1", 8000);
        socket.startHandshake();
        SSLSession session = socket.getSession();
        java.security.cert.Certificate[] servercerts = session.getPeerCertificates();

        List mylist = new ArrayList();
        for (int i = 0; i < servercerts.length; i++) {
          mylist.add(servercerts[i]);
        }

        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        CertPath cp = cf.generateCertPath(mylist);

        FileOutputStream f = new FileOutputStream("CertPath.dat");
        ObjectOutputStream b = new ObjectOutputStream(f);
        b.writeObject(cp);

        }

}
