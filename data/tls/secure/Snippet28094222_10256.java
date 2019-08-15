import org.apache.http.conn.ssl.SSLSocketFactory;
import com.amazonaws.ApacheHttpClientConfig;
import com.amazonaws.ClientConfiguration;

SSLContext ctx = SSLContext.getInstance("TLSv1.2");
SSLSocketFactory socketFactory = ctx.engineGetSocketFactory();

ClientConfiguration client = new ClientConfiguration();
ApacheHttpClientConfig apacheClient = client.getApacheHttpClientConfig();
SSLSocketFactory socketContext = apacheClient.getSslSocketFactory();
apacheClient.setSslSocketFactory(socketFactory);
