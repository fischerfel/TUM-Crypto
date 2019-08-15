import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
......
private static HttpClient getHttpClient() {

    try {
        SSLContext sslContext = SSLContext.getInstance("SSL");

        sslContext.init(null,
                new TrustManager[]{new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {

                        return null;
                    }

                    public void checkClientTrusted(
                            X509Certificate[] certs, String authType) {

                    }

                    public void checkServerTrusted(
                            X509Certificate[] certs, String authType) {

                    }
                }}, new SecureRandom());

        SSLConnectionSocketFactory socketFactory = new SSLConnectionSocketFactory(sslContext,SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);



        HttpClient httpClient = HttpClientBuilder.create().setSSLSocketFactory(socketFactory).build();

        return httpClient;

    } catch (Exception e) {
        e.printStackTrace();
        return HttpClientBuilder.create().build();
    }
}
