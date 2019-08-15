import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;
import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import org.junit.Test;

public class JUnitTest {

    private static final String TRUSTSTORE_FILE = "<location-of-truststore";
    private static final String TRUSTSTORE_PASSWORD = "trustStorePassword";

    @Test
    public void test() throws Exception {
        KeyStore truststore = KeyStore.getInstance("JKS");
        truststore.load(new FileInputStream(TRUSTSTORE_FILE), 
                                            TRUSTSTORE_PASSWORD.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(truststore);
        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, tmf.getTrustManagers(), null);

        ClientConfig config = new DefaultClientConfig();
        config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, 
                new HTTPSProperties(null, sslContext));
        Client client = Client.create(config);

        final String httpsUrl = "https://...";
        ClientResponse response = client.resource(httpsUrl).get(ClientResponse.class);
        System.out.println(response.getStatus());
        System.out.println(response.getEntity(String.class));
    } 
}
