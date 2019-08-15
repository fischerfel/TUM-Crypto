import java.io.FileInputStream;
import java.security.KeyStore;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

public class SimpleClientApp {

    private static final String TRUSTSTORE_FILE = "client-truststore.jks";
    private static final String TRUSTSTORE_PASSWORD = "trustpass";
    private static final String APP_URL 
            = "https://localhost:8443/secured-rest-app/webapi/myresource";

    public static void main(String[] args) throws Exception {
        KeyStore truststore = KeyStore.getInstance("JKS");
        truststore.load(new FileInputStream(TRUSTSTORE_FILE), 
                                            TRUSTSTORE_PASSWORD.toCharArray());
        TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
        tmf.init(truststore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        Client client = ClientBuilder.newBuilder()
                .sslContext(sslContext).build();
        client.register(HttpAuthenticationFeature.basic("peeskillet", "secret"));

        Response response = client.target(APP_URL).request().get();
        System.out.println(response.readEntity(String.class));

    }
}
