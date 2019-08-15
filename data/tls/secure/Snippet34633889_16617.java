package hello;

import java.net.URI;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import sun.security.ssl.SSLSocketFactoryImpl;

public class client {

    public static void main(String args[]) throws Exception {

//      System.setProperty("javax.net.debug", "ssl:handshake:verbose");

        SSLContext context = SSLContext.getInstance("TLSv1.2");
        context.init(null, null, null);

        SSLConnectionSocketFactory sslCF = new SSLConnectionSocketFactory(context, new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                // or add your own test here
                return true;
            }
        });

        CloseableHttpClient httpClient = HttpClientBuilder
                .create()
                .setSSLSocketFactory(sslCF)
                .build();

        HttpGet request = new HttpGet(new URI("https://www.wikipedia.org/"));
//        request.addHeader("Authorization", basicAuthorization);
//        request.addHeader("Accept", "text/plain");
        CloseableHttpResponse httpResponse = httpClient.execute(request);
        System.out.println(httpResponse.getStatusLine());           
    }
}
