import java.io.IOException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

FTPClient ftpsClient = new FTPClient();
try
{
    // Initialize the transfer information.
    this.oFtpArgs.oFtp.transferCount = this.oFtpArgs.pathFiles.size();
    this.oFtpArgs.oFtp.transferCompleted = 0;
    this.oFtpArgs.oFtp.filePercentComplete = 0L;
    this.oFtpArgs.oFtp.bytesTransferredTotal = 0L;

    // Connect to the server using active mode enabling FTP over explicit TLS/SSL (FTPES).
    ftpsClient.setSecurity(FTPClient.SECURITY_FTPES);
    ftpsClient.setPassive(false);
    ftpsClient.connect(this.oFtpArgs.serverIp, port);

    TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager()
    {
        @Override
        public X509Certificate[] getAcceptedIssuers(){return null;}
        @Override
        public void checkClientTrusted(X509Certificate[] certs, String authType){}
        @Override
        public void checkServerTrusted(X509Certificate[] certs, String authType){}
    }};

    // Install the all-trusting trust manager
    SSLContext sc = SSLContext.getInstance("TLS");
    sc.init(null, trustAllCerts, new SecureRandom());
    HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

    // Log into the server.
    ftpsClient.login(user, pass);
}

catch (IOException ex)
{
    System.out.println("Error: " + ex.getMessage());
    ex.printStackTrace();
}

catch (Exception ex)
{
    System.out.println("Error: " + ex.getMessage());
    ex.printStackTrace();
}
