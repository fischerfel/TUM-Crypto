import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;

import okhttp3.OkHttpClient;

public abstract class SSLClientFactory {

    private static boolean allowUntrusted = false;
    private static final long LOGIN_TIMEOUT_SEC = 10;
    private static HttpClientBuilder closeableClientBuilder = null;
    private static OkHttpClient.Builder okHttpClientBuilder = null;

    public enum HttpClientType{
        HttpClient,
        OkHttpClient
    } 


    public static synchronized ClientHttpRequestFactory getClientHttpRequestFactory(HttpClientType httpClientType){

        ClientHttpRequestFactory requestFactory = null;

        SSLContext sslContext = SSLClientFactory.getSSlContext();

        if(null == sslContext){
            return requestFactory;
        }

        switch (httpClientType) {

        case HttpClient:
            closeableClientBuilder = HttpClientBuilder.create();

            //Add the SSLContext and trustmanager
            closeableClientBuilder.setSSLContext(getSSlContext());
            //add the hostname verifier
            closeableClientBuilder.setSSLHostnameVerifier(gethostnameVerifier());   

            requestFactory = new HttpComponentsClientHttpRequestFactory(closeableClientBuilder.build());

            break;
        case OkHttpClient:
            okHttpClientBuilder = new OkHttpClient().newBuilder().readTimeout(LOGIN_TIMEOUT_SEC, TimeUnit.SECONDS);

            //Add the SSLContext and trustmanager
            okHttpClientBuilder.sslSocketFactory(getSSlContext().getSocketFactory(), getTrustManager());
            //add the hostname verifier
            okHttpClientBuilder.hostnameVerifier( gethostnameVerifier());

            requestFactory = new OkHttp3ClientHttpRequestFactory(okHttpClientBuilder.build());

            break;
        default:
            break;
        }

        return requestFactory;

    }


    private static SSLContext getSSlContext(){



        final TrustManager[] trustAllCerts = new TrustManager[]{getTrustManager()};

        SSLContext sslContext = null;
        try {

            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            e.printStackTrace();
        }



        return sslContext;

    }

    private static X509TrustManager getTrustManager(){

        final X509TrustManager trustManager = new X509TrustManager() {

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] cArrr = new X509Certificate[0];
                return cArrr;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // TODO Auto-generated method stub

            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                // TODO Auto-generated method stub

            }
        };

        return trustManager;
    }

    private static HostnameVerifier gethostnameVerifier(){

        HostnameVerifier hostnameVerifier = new HostnameVerifier() {

            @Override
            public boolean verify(String arg0, SSLSession arg1) {
                return true;
            }
        };

        return hostnameVerifier;

    }

}
