import java.io.IOException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

    public static HttpsURLConnection getConnection(boolean ignoreInvalidCertificate, String user, String pass, HttpRequestMethod httpRequestMethod, URL url) throws KeyManagementException, NoSuchAlgorithmException, IOException{
        SSLContext ctx = SSLContext.getInstance("TLS");
        if (ignoreInvalidCertificate){
            ctx.init(null, new TrustManager[] { new InvalidCertificateTrustManager() }, null);  
        }       
        SSLContext.setDefault(ctx);

        String authStr = user+":"+pass;
        String authEncoded = Base64.encodeBytes(authStr.getBytes());

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setRequestProperty("Authorization", "Basic " + authEncoded);     

        if (ignoreInvalidCertificate){
            connection.setHostnameVerifier(new InvalidCertificateHostVerifier());
        }

        return connection;
    }
