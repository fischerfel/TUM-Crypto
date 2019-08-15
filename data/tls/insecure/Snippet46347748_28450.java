package client;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.Hashtable;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TestClient {
    static String username = "uname";
    static String password = "password";
    static Hashtable<String,String> digHeaders = null;
    static String authValue = null;
    static String host = "192.168.102.102";

    public static void main(String[] args) {
        // Install the all-trusting host verifier
        OkHttpClient okHttpClient = getUnsafeOkHttpClient();
        Request request1 = buildRequest();
        try (Response response = okHttpClient.newCall(request1).execute()) {
            if (response.code() == 401) {
                getDigestValues(response);
            }
            //System.out.println(response.body().string());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        Request request2 = buildRequest();
        try (Response response = okHttpClient.newCall(request2).execute()) {
            System.out.println(response.code());
            if (response.code() == 200) {
                System.out.println(response.body().string());
            }
            //System.out.println(response.body().string());
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[] {
            new X509TrustManager() {
              @Override
              public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
              }

              @Override
              public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
              }

              @Override
              public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[]{};
              }
            }
        };

        // Install the all-trusting trust manager
        final SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        // Create an ssl socket factory with our all-trusting manager
        final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
        builder.hostnameVerifier(new HostnameVerifier() {
          @Override
          public boolean verify(String hostname, SSLSession session) {
            return true;
          }
        });

        OkHttpClient okHttpClient = builder.build();
        return okHttpClient;
      } catch (Exception e) {
        throw new RuntimeException(e);
      }
    }

    private static Request buildRequest() {
        if (digHeaders == null) {
            authValue =  "Digest username=\"uname\", realm="+host+", nonce=\"MsXzAI+I/NIu3n2R\", "
                + "uri=\"/sum/artwork\", qop=auth, nc=000002a5, cnonce=\"7bc6501580571223\", "
                + "response=\"12f4c2121928dde05c378c9858b1db0e\", opaque=\"QirDygkvC2dlD53u\"";
    } else {
        String nonce = ""+digHeaders.get("nonce");
        String opaque = ""+digHeaders.get("opaque");
        authValue =  "Digest username=\"uname\", realm="+host+", nonce="+nonce+", "
                + "uri=\"/sum/artwork\", qop=auth, nc=000002a5, cnonce=\"7bc6501580571223\", "
                + "response=\"12f4c2121928dde05c378c9858b1db0e\", opaque="+opaque+"";           
    }
    //System.out.println(authValue);
    Request request = new Request.Builder()
              .url("https://192.168.102.102/sum/artwork")
              .get()
              .addHeader("host", "192.168.102.102")
              .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0")
              .addHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
              .addHeader("accept-language", "en-US,en;q=0.5")
              .addHeader("accept-encoding", "gzip, deflate, br")
              .addHeader("cookie", "NTPClockOffset=356773|1496929394739; abcde=YTc4YmQ0MGYtYjEyYy00ZjU0LTRmMjQtMDFkYTk1YWI2YjQz")
              .addHeader("connection", "keep-alive")
              .addHeader("upgrade-insecure-requests", "1")
              .addHeader("authorization", authValue) 
              .addHeader("cache-control", "no-cache")
              .addHeader("postman-token", "91405146-94f9-b370-b803-26997f8ac466")
              .build();
    //printHeaders(request.headers());
    return(request);
    }

    private static void getDigestValues(Response response) {
        Hashtable<String,String> temp = new Hashtable<String,String>();
        Headers responseHeaders = response.headers();
        for (int i = 0; i < responseHeaders.size(); i++) {
            //System.out.println(responseHeaders.name(i) + ": " + responseHeaders.value(i));
            if (responseHeaders.name(i).trim().equals("Www-Authenticate")) {
                //System.out.println("This is www");
                String[] vals = responseHeaders.value(i).split(",");
                for (String val : vals) {
                    //System.out.println(val);
                    String[] pair = val.trim().split("=");
                    if (pair[0].equals("nonce")||(pair[0].equals("opaque"))) {
                        temp.put(pair[0], pair[1]);
                    }
                }
            }
        }
        //System.out.println(temp);
        digHeaders = temp;
    }

    private static void printHeaders(Headers headers) {
        for (int i=0; i<headers.size(); i++) {
            System.out.println(headers.name(i) + ": " + headers.value(i));
        }
    }
}
