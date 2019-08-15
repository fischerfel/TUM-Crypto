            import com.sun.jersey.api.client.Client;
            import com.sun.jersey.api.client.ClientResponse;
            import com.sun.jersey.api.client.WebResource;
            import com.sun.jersey.client.urlconnection.HttpURLConnectionFactory;
            import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

            import com.sun.jersey.core.util.Base64;

            import java.io.IOException;
            import java.net.HttpURLConnection;
            import java.net.InetSocketAddress;
            import java.net.Proxy;
            import java.net.URL;
            import java.security.KeyManagementException;
            import java.security.NoSuchAlgorithmException;
            import java.security.SecureRandom;
            import java.util.logging.Level;
            import java.util.logging.Logger;
            import javax.net.ssl.HostnameVerifier;
            import javax.net.ssl.HttpsURLConnection;
            import javax.net.ssl.SSLContext;
            import javax.net.ssl.TrustManager;
            import java.security.cert.CertificateException;
            import java.security.cert.X509Certificate;
            import javax.net.ssl.X509TrustManager;
            import javax.ws.rs.core.MediaType;

            public class ConnectionFactory implements HttpURLConnectionFactory {

                Proxy proxy;

                String proxyHost = //ener your proxy host

                Integer proxyPort = //ener your proxy port

                SSLContext sslContext;

                public ConnectionFactory() {
                }

                public ConnectionFactory(String proxyHost, Integer proxyPort) {
                    this.proxyHost = proxyHost;
                    this.proxyPort = proxyPort;
                }

                private void initializeProxy() {
                    proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                }

                @Override
                public HttpURLConnection getHttpURLConnection(URL url) throws IOException {
                    initializeProxy();
                    HttpURLConnection con = (HttpURLConnection) url.openConnection(proxy);
                    if (con instanceof HttpsURLConnection) {
                        System.out.println("The valus is....");
                        HttpsURLConnection httpsCon = (HttpsURLConnection) url.openConnection(proxy);
                        httpsCon.setHostnameVerifier(getHostnameVerifier());
                        httpsCon.setSSLSocketFactory(getSslContext().getSocketFactory());
                        return httpsCon;
                    } else {
                        return con;
                    }

                }

                public SSLContext getSslContext() {
                    try {
                        sslContext = SSLContext.getInstance("SSL");
                        sslContext.init(null, new TrustManager[]{new SecureTrustManager()}, new SecureRandom());
                    } catch (NoSuchAlgorithmException ex) {
                       // Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (KeyManagementException ex) {
                       // Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    return sslContext;
                }

                private HostnameVerifier getHostnameVerifier() {
                    return new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname,
                                javax.net.ssl.SSLSession sslSession) {
                            return true;
                        }
                    };
                }
                public static void main(String[] args){
                    URLConnectionClientHandler cc = new URLConnectionClientHandler(new ConnectionFactory("enter your proxy host",enter your proxy port));
                    Client client = new Client(cc);
                    client.setConnectTimeout(2000000);
                    WebResource resource = client.resource("host url");
                    resource.type(MediaType.APPLICATION_JSON);
                    ClientResponse response =resource .post(ClientResponse.class, "");      
                    String resp = response.getEntity(String.class);
                    //String decode = Base64.base64Decode(resp);
                    System.out.println(resp);
                }
            }


            class SecureTrustManager implements X509TrustManager {

                @Override
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                        throws CertificateException {
                }

                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public boolean isClientTrusted(X509Certificate[] arg0) {
                    return true;
                }

                public boolean isServerTrusted(X509Certificate[] arg0) {
                    return true;
                }

            }
