package test;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

@SuppressWarnings("unused")
public class Test {
    private String filePath;

    private InputStream inputStream = null;

    private DefaultHttpClient hc = null;

    public Test(String filePath) {
        this.filePath = filePath;

        try {
            this.inputStream = new FileInputStream(filePath);

            if (this.inputStream != null) {
                Certificate myCert = CertificateFactory.getInstance("X.509")
                        .generateCertificate(this.inputStream);

                KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
                keyStore.load(null, null);
                keyStore.setCertificateEntry("myCert", myCert);

                SSLSocketFactory sf = new EasySSLSocketFactory(keyStore);
                sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

                HttpParams params = new BasicHttpParams();
                HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
                HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
                HttpProtocolParams.setUseExpectContinue(params, true);

                SchemeRegistry registry = new SchemeRegistry();
                registry.register(new Scheme("http", PlainSocketFactory
                        .getSocketFactory(), 80));
                registry.register(new Scheme("https", sf, 443));

                ClientConnectionManager ccm = new ThreadSafeClientConnManager(
                        params, registry);

                hc = new DefaultHttpClient(ccm, params);

                // Prepare a request object
                String url = "https://service.cashyr.com:8443/PolarBear-0.0.1-SNAPSHOT/get_deals_dist/33.7445273910949/-118.10924671590328/12/0/aaaa";
                HttpGet httpget = new HttpGet(url);

                // Execute the request
                HttpResponse response;

                InputStream instream = null;
                String data = null;

//                // setSeeMoreDealsButton(context, false);
                response = hc.execute(httpget);
                HttpEntity entity = response.getEntity();

                instream = entity.getContent();
                if (instream != null) {
                    data = Utils.convertStreamToString(instream);
                    System.out.println(data);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();

            hc = new DefaultHttpClient();
        }
        catch (Exception e) {
            e.printStackTrace();

            hc = new DefaultHttpClient();
        }
    }

    public static void main(String[] args) {
        new Test("service.cashyr.com.crt");
    }
}
