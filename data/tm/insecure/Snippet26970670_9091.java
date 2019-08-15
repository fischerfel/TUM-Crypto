import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Credentials;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class TestOkHttp {

static public void main(String argcp[]){
    try {
        OkHttpClient okHttpClient = new OkHttpClient();
          TrustManager[] trustAllCerts = new TrustManager[] {
                   new X509TrustManager() {

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        // TODO Auto-generated method stub
                        return null;
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] arg0, String arg1)
                            throws CertificateException {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void checkClientTrusted(X509Certificate[] arg0, String arg1)
                            throws CertificateException {
                        // TODO Auto-generated method stub

                    }
                }};

                SSLContext sc = SSLContext.getInstance("TLS");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                okHttpClient.setSslSocketFactory(sc.getSocketFactory());
            Proxy p=new Proxy(Proxy.Type.HTTP,new InetSocketAddress("10.1.1.0", 8080));
          okHttpClient.setProxy(p);
            HostnameVerifier hostNameVerifier = new HostnameVerifier() {

                @Override
                public boolean verify(String sourceHost, SSLSession arg1) {
                    // TODO Auto-generated method stub
                    return sourceHost.equals("serverwithtls.com");
                }

            };

            okHttpClient.setHostnameVerifier(hostNameVerifier);
        okHttpClient.setAuthenticator(new Authenticator() {

            @Override
            public Request authenticateProxy(Proxy proxy, Response response)
                    throws IOException {
                 String credential = Credentials.basic("username","password");
                            return response.request().newBuilder()
                                .header("Authorization", credential)
                                .build();               
                            }

            @Override
            public Request authenticate(Proxy arg0, Response arg1)
                    throws IOException {
                // TODO Auto-generated method stub
                return null;
            }


        });
         Request request = new Request.Builder()
            .url("https://serverwithtls.com")
            .build();

        Response response = okHttpClient.newCall(request).execute();
        System.out.println(response.body().string());
    } catch (Exception e) {
        e.printStackTrace();
    }

}
}
