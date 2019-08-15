import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.ClientResponse;




public class Harvest {

    public static void main(String[] args) throws KeyManagementException, 
        NoSuchAlgorithmException, NullPointerException {

        SSLContext sslContext = SSLContext.getInstance("SSL");
        HostnameVerifier
           hostnameVerifier=HttpsURLConnection.getDefaultHostnameVerifier();

        SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[20];
        random.nextBytes(bytes);

        sslContext.init(null, null, random);

        Client client = ClientBuilder.newBuilder()
                .sslContext(sslContext)
                .hostnameVerifier(hostnameVerifier)
                .build();


        WebTarget target = client.target("https://peakhosting.pagerduty.com/api/v1/schedules");
        Invocation.Builder builder;
        builder = target.request();
        builder.header("Authorization", "Token token=<secret>");
        builder.accept(MediaType.APPLICATION_JSON);  

-->     ClientResponse response = builder.accept("application/json").get(ClientResponse.class);
        System.out.println(response.getStatus());

        if (response.getStatus() != 200) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }


    }
}
