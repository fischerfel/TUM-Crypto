import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.junit.Assert;
import org.junit.Test;

import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.providers.jdk.JDKAsyncHttpProvider;

public class DH480Test extends Assert {

    private final String URL = "https://dh480.badssl.com/";

    @Test
    public void testNingHTTPClientWithJDKProvider() {
        AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder().build();
        AsyncHttpClient client = new AsyncHttpClient(new JDKAsyncHttpProvider(config), config);

        try {
            client.prepareGet(URL).execute().get();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testApacheHTTClient() throws NoSuchAlgorithmException, KeyManagementException {
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, null, null);

        Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("https", new SSLConnectionSocketFactory(sslContext))
                .register("http", new PlainConnectionSocketFactory())
                .build();
        HttpClientConnectionManager ccm = new BasicHttpClientConnectionManager(registry);
        HttpClientBuilder builder = HttpClientBuilder.create();
        builder.setConnectionManager(ccm);

        HttpClient client = builder.build();

        try {
            HttpGet get = new HttpGet(URL);
            HttpResponse r = client.execute(get);
            EntityUtils.consume(r.getEntity());
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
