import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.HTTPSProperties;




TrustManager[] certs = new TrustManager[] {new X509TrustManager() {
    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {}

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {}
}};

private Client getSSLClient() throws NoSuchAlgorithmException, KeyManagementException {
    HostnameVerifier hostnameVerifier = HttpsURLConnection.getDefaultHostnameVerifier();
    ClientConfig config = new DefaultClientConfig();
    SSLContext ctx = SSLContext.getInstance("TLS");
    ctx.init(null, this.certs, null);
    config.getProperties().put(HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, new HTTPSProperties(hostnameVerifier, ctx));
    Client client = Client.create(config);
    return client;
}
