import java.io.IOException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class TestSsl {

    HttpClient httpclient = new DefaultHttpClient();

    public static void main(String[] args) throws Exception {

        TestSsl t = new TestSsl();
        t.init();
        t.queryData();
    }

    private void init() {
        httpclient = wrapClient(httpclient);
        httpclient.getParams()
                .setIntParameter("http.socket.timeout", 30 * 1000);
    }

    /**
     * 
     * @throws Exception
     */
    private void queryData() throws Exception {
        HttpGet httpget = new HttpGet("https://www.grubhub.com/browse/");
        HttpResponse response = httpclient.execute(httpget);

        HttpEntity entity = response.getEntity();
        String result = EntityUtils.toString(entity);
        System.out.println(result);
        EntityUtils.consume(entity);

    }

    /**
     * 
     * @param base
     * @return
     */
    public static HttpClient wrapClient(HttpClient base) {
        try {
            SSLContext ctx = SSLContext.getInstance("SSL");
            X509TrustManager tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] xcs,
                        String string) {
                }

                public void checkServerTrusted(X509Certificate[] xcs,
                        String string) {
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
            };
            X509HostnameVerifier verifier = new X509HostnameVerifier() {

                @Override
                public void verify(String string, SSLSocket ssls) throws IOException {
                }

                @Override
                public void verify(String string, X509Certificate xc) throws SSLException {
                }

                @Override
                public void verify(String string, String[] strings, String[] strings1) throws SSLException {
                }

                @Override
                public boolean verify(String string, SSLSession ssls) {
                    return true;
                }
            };

            ctx.init(null, new TrustManager[] { tm }, null);
            SSLSocketFactory ssf = new SSLSocketFactory(ctx);
            ssf.setHostnameVerifier(verifier);
            ClientConnectionManager ccm = base.getConnectionManager();
            SchemeRegistry sr = ccm.getSchemeRegistry();
            sr.register(new Scheme("https", ssf, 443));
            return new DefaultHttpClient(ccm, base.getParams());
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
