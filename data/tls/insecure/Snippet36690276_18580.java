package com.rest.client;
    //http://apiwave.com/java/snippets/removal/org.glassfish.jersey.client.authentication.HttpAuthenticationFeature
    import java.io.IOException;
    import java.net.*;
    import java.security.KeyManagementException;
    import java.security.NoSuchAlgorithmException;
    import javax.net.ssl.HostnameVerifier;
    import javax.net.ssl.SSLContext;
    import javax.net.ssl.TrustManager;
    import javax.ws.rs.client.Client;
    import javax.ws.rs.client.ClientBuilder;
    import javax.ws.rs.client.Entity;
    import javax.ws.rs.client.WebTarget;
    import javax.ws.rs.core.Response;
    import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
    import org.glassfish.jersey.filter.LoggingFilter;
    import com.rest.dto.Employee;

    public class RestClientTest {
        /**
         * @param args
         */
        public static void main(String[] args) {
            try {
                //
                sslRestClientGETReport();
                //
                sslRestClientPost();
                //
                sslRestClientGET();
                //
            } catch (KeyManagementException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        }

        //
        private static WebTarget    target      = null;
        //
        private static String       userName    = "Vkhan";
        private static String       passWord    = "Vkhan";

        //
        public static void sslRestClientGETReport() throws KeyManagementException, IOException, NoSuchAlgorithmException {
            //
            //AuthService 

            //
            SSLContext sc = SSLContext.getInstance("TLSv1");
            TrustManager[] trustAllCerts = { new InsecureTrustManager() };
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HostnameVerifier allHostsValid = new InsecureHostnameVerifier();
            //
            Client c = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
            //


            String baseUrl ="https://vaquarkhan.net/companyabc/efgd//criteria";

            c.register(HttpAuthenticationFeature.basic(userName, passWord));
            target = c.target(baseUrl);
            target.register(new LoggingFilter());
            String responseMsg = target.request().get(String.class);
            System.out.println("-------------------------------------------------------");
            System.out.println(responseMsg);
            System.out.println("-------------------------------------------------------");
            //

        }

        public static void sslRestClientGET() throws KeyManagementException, IOException, NoSuchAlgorithmException {
            //
            //
            SSLContext sc = SSLContext.getInstance("SSL");
            TrustManager[] trustAllCerts = { new InsecureTrustManager() };
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HostnameVerifier allHostsValid = new InsecureHostnameVerifier();
            //
            Client c = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
            //

            String baseUrl = "https://vaquarkhan.net/companyabc/efgd//criteria";

            //
            c.register(HttpAuthenticationFeature.basic(userName, passWord));
            target = c.target(baseUrl);
            target = target.path("vm/khan/api/v1/name").queryParam("search","%7B\"aa\":\"202\",\"bb\":\"khan\",\"tt\":\"10\",\"type\":\"OP\",\"userId\":[\"123,456\"],\"nk\":\"IM\",\"pk\":\"op\"%7D");

            target.register(new LoggingFilter());
            String responseMsg = target.request().get(String.class);
            System.out.println("-------------------------------------------------------");
            System.out.println(responseMsg);
            System.out.println("-------------------------------------------------------");
            //

        }
        //TOD need to fix
        public static void sslRestClientPost() throws KeyManagementException, IOException, NoSuchAlgorithmException {
            //
            //
            Employee employee = new Employee("123", "12345", "20", "KK",
                    null, "6786", "dfdfdf", "we", "sdsdsdsds", "4", "4");
            //
            SSLContext sc = SSLContext.getInstance("SSL");
            TrustManager[] trustAllCerts = { new InsecureTrustManager() };
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HostnameVerifier allHostsValid = new InsecureHostnameVerifier();
            //
            Client c = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
            //

            String baseUrl = "https://vaquar/khan/api/v1/name";

            c.register(HttpAuthenticationFeature.basic(userName, passWord));
            target = c.target(baseUrl);
            target.register(new LoggingFilter());
            //
            Response response = target.request().put(Entity.json(employee));
            String output = response.readEntity(String.class);
            //
            System.out.println("-------------------------------------------------------");
            System.out.println(output);
            System.out.println("-------------------------------------------------------");

        }

        public static void URI( String myURL){

            try {
              URL url = new URL(myURL);
              String nullFragment = null;
              URI uri = new java.net.URI(url.getProtocol(), url.getHost(), url.getPath(), url.getQuery(), nullFragment);
              System.out.println("URI " + uri.toString() + " is OK");
            } catch (MalformedURLException e) {
              System.out.println("URL " + myURL + " is a malformed URL");
            } catch (URISyntaxException e) {
              System.out.println("URI " + myURL + " is a malformed URL");
            }
          }
    }
