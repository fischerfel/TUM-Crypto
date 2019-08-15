import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;

public class JerseyClientPost {

public static void main(String[] args) {

    try {
        DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
        SSLContext sslContext = SSLContext.getInstance("TLS");
        // ServerTrustManager serverTrustManager = new ServerTrustManager();
        sslContext.init(null, new TrustManager[] {}, null);
        defaultClientConfig.getProperties().put(
                HTTPSProperties.PROPERTY_HTTPS_PROPERTIES,
                new HTTPSProperties(null, sslContext));
        Client client = Client.create(defaultClientConfig);

        WebResource webResource = client
                .resource("https://xxxxxxxyyyyyyzzzzzzz-vp0.us.blockchain.ibm.com:5003/chaincode");

        String input = "{\"jsonrpc\": \"2.0\",\"method\": \"invoke\",\"params\": {\"type\": 1,\"chaincodeID\": {\"name\": \"dfc74e1d2544612828ec99295946a9e352ce689668b0f5c9a4eb587b4858ca2812c0bddd171004b09acf9ea0c1d0f55ddd410ad5856a5189c291cc9cc957f018\"},\"ctorMsg\": {\"function\": \"init_marble\",\"args\": [\"marbel_2\",\"blue\",\"35\",\"amit\"]},\"secureContext\": \"user_type1_4\"},\"id\": 1490508638375}";

        ClientResponse response = webResource.type("application/json")
                .post(ClientResponse.class, input);

        if (response.getStatus() != 201) {
            throw new RuntimeException("Failed : HTTP error code : "
                    + response.getStatus());
        }

        System.out.println("Output from Server .... \n");
        String output = response.getEntity(String.class);
        System.out.println(output);

    } catch (NoSuchAlgorithmException e) {
        System.out.println("No such algo excaption");
        e.printStackTrace();
    } catch (KeyManagementException e) {
        System.out.println("Key Management Exception.");
    }

}
}
