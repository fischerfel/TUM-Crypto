import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

public class DownloadFileExample
{
    public static void download(String downloadURL) throws IOException
    {
        URL website = new URL(downloadURL);
        String fileName = "downloaded.zip";

        try (InputStream inputStream = website.openStream())
        {
            Files.copy(inputStream, Paths.get(fileName), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private static void trustAllCertificates() throws NoSuchAlgorithmException, KeyManagementException
    {
        TrustManager[] trustManagers = new TrustManager[]{new X509TrustManager()
        {
            public X509Certificate[] getAcceptedIssuers()
            {
                return new X509Certificate[0];
            }

            public void checkClientTrusted(
                    X509Certificate[] certs, String authType)
            {
            }

            public void checkServerTrusted(
                    X509Certificate[] certs, String authType)
            {
            }
        }};

        SSLContext sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, trustManagers, new SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
    }

    public static void main(String[] arguments) throws IOException, NoSuchAlgorithmException, KeyManagementException
    {
        trustAllCertificates();

        String downloadURL = "https://mh-nexus.de/downloads/HxDSetupEN.zip";
        download(downloadURL);
    }
}
