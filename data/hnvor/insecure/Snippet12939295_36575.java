import java.net.*;
import java.security.*;
import java.security.cert.*;
import javax.net.ssl.*;
import java.io.*;
import java.util.*;

public class PostTest {
    static HttpsURLConnection conn = null;
    static String sessionId = null;
    private static class DefaultTrustManager implements X509TrustManager {

        @Override
        public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            ctx.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(ctx);


            String data = URLEncoder.encode("txtUserName", "UTF-8") + "=" + URLEncoder.encode(/*username*/, "UTF-8");
            data += "&" + URLEncoder.encode("txtPassword", "UTF-8") + "=" + URLEncoder.encode(/*password*/", "UTF-8");
            data += "&" + URLEncoder.encode("envia", "UTF-8") + "=" + URLEncoder.encode("1", "UTF-8");

            connectToSSL(/*login url*/);

                    //throws java.lang.IllegalStateException: Already connected
            conn.setDoOutput(true); 

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String resposta = "";
            while((line = rd.readLine()) != null) {
                resposta += line + "\n";
            }
            System.out.println("valid login -> " + resposta.contains(/*string that assures me I'm looged in*/));

            connectToSSL(/*first navigation page*/);

            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            while((line = rd.readLine()) != null) {
                System.out.println(line);
            }


        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    private static void connectToSSL(String address) {
        try {
            URL url = new URL(address);
            conn = (HttpsURLConnection) url.openConnection();
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });
                    if(sessionId == null) {
                            sessionId = conn.getHeaderField("Set-Cookie");
                    }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

}
