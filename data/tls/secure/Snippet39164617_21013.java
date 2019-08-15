import javax.net.ssl.SSLContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

SSLContext context = SSLContext.getInstance("TLSv1.2");
context.init(null, null, null);

CloseableHttpClient httpClient = HttpClientBuilder
        .create()
        .setSSLContext(context)
        .build();
HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
RestTemplate restTemplate = new RestTemplate(factory);
.....
