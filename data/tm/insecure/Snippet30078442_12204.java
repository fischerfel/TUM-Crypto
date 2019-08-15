import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;

public class ConnectToUrlUsingBasicAuthentication
{
    static
    {
        disableSslVerification();
    }

    private static void disableSslVerification()
    {
        try
        {
            // Create a trust manager that does not validate certificate chains
            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
            };

            // Install the all-trusting trust manager
            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

            // Create all-trusting host name verifier
            HostnameVerifier allHostsValid = new HostnameVerifier()
            {
                public boolean verify(String hostname, SSLSession session)
                {
                    return true;
                }
            };

            // Install the all-trusting host verifier
            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (KeyManagementException e)
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args)
    {
        try
        {
            String webPage = "https://AAA.BBB.CCC.DDD:1234/login";
            String name = "xxx";
            String password = "yyy";

            String authString = name + ":" + password;
            System.out.println("auth string: " + authString);
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
            String authStringEnc = new String(authEncBytes);
            System.out.println("Base64 encoded auth string: " + authStringEnc);

            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setRequestProperty("Authorization", "Basic " + authStringEnc);
            javax.net.ssl.HostnameVerifier myHv = new javax.net.ssl.HostnameVerifier(){
                public boolean verify(String
                        hostName,javax.net.ssl.SSLSession session) { return true; } 
            };
            ((HttpsURLConnection) urlConnection).setHostnameVerifier(myHv); urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0)
            {
                sb.append(charArray, 0, numCharsRead);
            }
            String result = sb.toString();

            System.out.println("*** BEGIN ***");
            System.out.println(result);
            System.out.println("*** END ***");
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
