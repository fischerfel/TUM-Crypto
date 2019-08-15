import java.io.*;
import java.net.*;
import java.security.*;
import javax.net.ssl.*;
import org.apache.http.*;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.*;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.params.*;
import org.apache.http.entity.StringEntity;

public class Test {
    public static void main(String[] args) {
        String keyStorePath = "C:\\java\\keys\\client_certificate.p12";
        String keyStorePass = "someKeyStorePass";
        String trustStorePath = "C:\\java\\keys\\truststore.jks";
        String trustStorePass = "someTrustStorePath";
        String postData = "<request></request>";
        String url = "https://api.some-url.com/v2";

        SSLContext sslContext = GetSSLContext(keyStorePath, keyStorePass, trustStorePath, trustStorePass);
        PostData1(postData, url, sslContext);

        SchemeRegistry schemeRegistry = GetSchemeRegistry(keyStorePath, keyStorePass, trustStorePath, trustStorePass);
        PostData2(postData, url, schemeRegistry);
    }

    public static void PostData1(String dataToPost, String serviceUrl, SSLContext sslContext) {
        try {
            URL url = new URL(serviceUrl);

            HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
            HttpsURLConnection urlConn = (HttpsURLConnection)url.openConnection();
            urlConn.setDoOutput(true);
            urlConn.setDoInput(true);
            urlConn.setUseCaches(false);
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Content-Type", "application/xml");
            urlConn.connect();

            PrintWriter out = new PrintWriter(urlConn.getOutputStream());
            out.println(dataToPost);
            out.close();

            System.out.println("Response Code 1: " + urlConn.getResponseCode());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void PostData2(String dataToPost, String serviceUrl, SchemeRegistry schemeRegistry) {
        try {
            HttpParams httpParams = new BasicHttpParams();
            DefaultHttpClient httpClient = new DefaultHttpClient(new BasicClientConnectionManager(schemeRegistry), httpParams);

            HttpPost httpPost = new HttpPost(serviceUrl);

            HttpEntity lEntity = new StringEntity(dataToPost, "UTF-8");
            httpPost.setEntity(lEntity);

            HttpResponse response = httpClient.execute(httpPost);

            try {
                System.out.println("Response Code 2: " + response.getStatusLine().getStatusCode());
            } finally {
                httpPost.releaseConnection();
            }

            httpClient.getConnectionManager().shutdown();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static SSLContext GetSSLContext(String clientStorePath, String clientStorePass, String trustStorePath, String trustStorePass) {
        SSLContext sslContext = null;

        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            clientStore.load(new FileInputStream(clientStorePath), clientStorePass.toCharArray());

            KeyManagerFactory kmf =KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(clientStore, clientStorePass.toCharArray());
            KeyManager[] kms = kmf.getKeyManagers();

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(trustStorePath), trustStorePass.toCharArray());

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            TrustManager[] tms = tmf.getTrustManagers();

            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(kms, tms, new SecureRandom());

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return sslContext;
    }

    public static SchemeRegistry GetSchemeRegistry(String clientStorePath, String clientStorePass, String trustStorePath, String trustStorePass) {
        final SchemeRegistry schemeRegistry = new SchemeRegistry();

        try {
            KeyStore clientStore = KeyStore.getInstance("PKCS12");
            clientStore.load(new FileInputStream(clientStorePath), clientStorePass.toCharArray());

            KeyStore trustStore = KeyStore.getInstance("JKS");
            trustStore.load(new FileInputStream(trustStorePath), trustStorePass.toCharArray());

            schemeRegistry.register(new Scheme("https", 443, new SSLSocketFactory(clientStore, clientStorePass, trustStore)));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return schemeRegistry;
    }
}
