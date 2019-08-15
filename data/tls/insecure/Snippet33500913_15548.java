package com.rest.client;
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
import com.rest.dto.EarUnearmarkCollateralInput;

public class RestClientTest {
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            //
            sslRestClientGETReport();
            //
            sslRestClientPostEarmark();
            //
            sslRestClientGETRankColl();
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
    private static String       userName    = "username";
    private static String       passWord    = "password";

    //
    public static void sslRestClientGETReport() throws KeyManagementException, IOException, NoSuchAlgorithmException {
        //
        //
        SSLContext sc = SSLContext.getInstance("SSL");
        TrustManager[] trustAllCerts = { new InsecureTrustManager() };
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HostnameVerifier allHostsValid = new InsecureHostnameVerifier();
        //
        Client c = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
        //
        String baseUrl = "https://localhost:7002/VaquarKhanWeb/employee/api/v1/informations/report";
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
        //Query param Search={JSON}
        //
        SSLContext sc = SSLContext.getInstance("SSL");
        TrustManager[] trustAllCerts = { new InsecureTrustManager() };
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HostnameVerifier allHostsValid = new InsecureHostnameVerifier();
        //
        Client c = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
        //
        String baseUrl = "https://localhost:7002/VaquarKhanWeb";

        //
        c.register(HttpAuthenticationFeature.basic(userName, passWord));
        target = c.target(baseUrl);
        target = target.path("employee/api/v1/informations/employee/data").queryParam("search","%7B\"name\":\"vaquar\",\"surname\":\"khan\",\"age\":\"30\",\"type\":\"admin\""%7D");

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
        Employee employee = new Employee("vaquar", "khan", "30", "E");
        //
        SSLContext sc = SSLContext.getInstance("SSL");
        TrustManager[] trustAllCerts = { new InsecureTrustManager() };
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HostnameVerifier allHostsValid = new InsecureHostnameVerifier();
        //
        Client c = ClientBuilder.newBuilder().sslContext(sc).hostnameVerifier(allHostsValid).build();
        //
        String baseUrl = "https://localhost:7002/VaquarKhanWeb/employee/api/v1/informations/employee";
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


}
