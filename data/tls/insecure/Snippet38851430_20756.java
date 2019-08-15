import java.io.FileInputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPublicKey;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.jboss.security.Base64Encoder;

public class GetCertificates {
    static private TrustManager[] trustmgr = new TrustManager[] { new X509TrustManager() {

        private X509Certificate[] compCerts = null;

        public void checkClientTrusted(X509Certificate[] certs, String authType) {
            System.out.println("checkClientTrusted");
        }

        public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
            boolean match = false;
            compCerts = getcompanyCerts("C:/Users/vinod/Desktop/certificate tests/googleCert.cer");

            for (int i = 0; i < certs.length; i++) {
                for (int j = 0; j < compCerts.length; j++) {
                    PublicKey pubKey = compCerts[j].getPublicKey();
                    if (certs[i].getPublicKey().equals(pubKey)) {
                        match = true;
                        break;
                    }
                }
            }
            if (!match) {
                compCerts = null;
                throw new CertificateException();
            }

            System.out.println("checkServerTrusted");
        }

        public X509Certificate[] getAcceptedIssuers() {
            System.out.println("getAcceptedIssuers");
            return compCerts;
        }
    } };

    public void postMessage() {
        try {

            String server = "google.com";//github.com, google.com, 
            int port = 443;
            String protocol = "https";
            String authData = "";
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustmgr, new SecureRandom());
            SSLSocketFactory sf = new SSLSocketFactory(sslContext, new AllowAllHostnameVerifier());
            Scheme httpsScheme = new Scheme("https", port, sf);
            SchemeRegistry schemeRegistry = new SchemeRegistry();
            schemeRegistry.register(httpsScheme);
            ClientConnectionManager cm = new SingleClientConnManager(schemeRegistry);
            DefaultHttpClient httpclient = new DefaultHttpClient(cm);
            HttpResponse httpClientResponse = null;
            HttpRequestBase httpBase = null;

            HttpGet httpGet = new HttpGet(protocol + "://" + server + ":" + String.valueOf(port));

            httpGet.addHeader("Authorization", "Basic " + authData);
            httpGet.addHeader("accept", "application/xml");
            httpBase = httpGet;
            httpClientResponse = httpclient.execute(httpBase);
            System.out.println(httpClientResponse.getStatusLine().getStatusCode());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static X509Certificate[] getcompanyCerts(String path)
    {
        try
        {
            X509Certificate x509Certificate[] = null;
            ArrayList<X509Certificate> serverCerts = new ArrayList<X509Certificate>();
            Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate(new FileInputStream(path));
            X509Certificate cert1 = (X509Certificate) cert;
            serverCerts.add(cert1);
            x509Certificate = serverCerts.toArray(new X509Certificate[serverCerts.size()]);
            return x509Certificate;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        new GetCertificates().postMessage();
    }

}
