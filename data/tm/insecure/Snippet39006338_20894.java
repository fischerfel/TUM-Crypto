import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.PasswordAuthentication;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLConnect {

        public static void main(String[] args) throws Exception {

            String urlString = System.getProperty("url", "https://yourURLgoesHere:8443/test?");
            CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
            Authenticator.setDefault(new MyAuthenticator("domainname\\yourname", "yourpassword"));


            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            HttpsURLConnection httpsUrlConnection = (HttpsURLConnection) urlConnection;
            SSLSocketFactory sslSocketFactory = createTrustAllSslSocketFactory();
            httpsUrlConnection.setSSLSocketFactory(sslSocketFactory);


            try (InputStream inputStream = httpsUrlConnection.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String line = null;
                while ((line = reader.readLine()) != null) {
              // if you want to print the content
                  System.out.println(line);

                }
            }
        }

      // Trust any Server that provides the SSL certificate by bypassing trust managers 

        private static SSLSocketFactory createTrustAllSslSocketFactory() throws Exception {
            TrustManager[] byPassTrustManagers = new TrustManager[] { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) {
                }
            } };
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, byPassTrustManagers, new SecureRandom());
            return sslContext.getSocketFactory();
        }

}

// Authenticator which intercepts and provide required credential

class MyAuthenticator extends Authenticator {
    private String httpUsername;
    private String httpPassword;

    public MyAuthenticator(String httpUsername, String httpPassword) {
        this.httpUsername = httpUsername;
        this.httpPassword = httpPassword;
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        System.out.println("Scheme:" + getRequestingScheme());
        return new PasswordAuthentication(httpUsername, httpPassword.toCharArray());
    }
}
