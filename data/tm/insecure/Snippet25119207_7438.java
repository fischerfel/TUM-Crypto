import java.net.*;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.io.*;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.apache.axis2.java.security.TrustAllTrustManager;
import org.json.JSONObject;

public class httpsurl implements X509TrustManager {



public static void main(String s[]) throws ProtocolException {
    httpsurl.query("https://instance.service-now.com/incident.do?JSON");

}

public static void query(String URLName) {
    HttpsURLConnection con = null;
    try {

        JSONObject json = new JSONObject();
        json.put("parameter 1", "value 1");
        json.put("parameter 2", "value 2");



        sun.misc.BASE64Encoder encoder = new sun.misc.BASE64Encoder();
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(
                "proxy name", 8080));
        String encodedUserPwd = encoder
                .encode("username:password".getBytes());
          URL u = new URL(URLName);
  //            System.setProperty("http.keepAlive", "false");

        con = (HttpsURLConnection) u.openConnection(proxy);
        SSLContext mySsl = SSLContext.getInstance("TLS");
         mySsl.init(null, new TrustManager[]{new TrustAllTrustManager()}, null);
        con.setRequestProperty("connection", "close");
        con.setReadTimeout(10000);
         con.setConnectTimeout(15000);
        con.setRequestMethod("POST");
         con.setDoInput(true);
        con.setDoOutput(true);
        con.addRequestProperty("Content-Type",  "application/json");
        con.setRequestProperty("Authorization", "Basic " + encodedUserPwd);
         con.setUseCaches(false);
        con.setSSLSocketFactory(mySsl.getSocketFactory());

//          con.connect();          

        OutputStream os = con.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                os, "UTF-8"));
        writer.write(json.toString());
          writer.flush();
        writer.close();
        os.close();



         // Get response
        int HttpsResult = con.getResponseCode();
        StringBuffer sb = new StringBuffer();
        if (HttpsResult == HttpsURLConnection.HTTP_OK) {
             BufferedReader br = new BufferedReader(new InputStreamReader(
                     con.getInputStream(), "utf-8"));
             String line = null;

             while ((line = br.readLine()) != null) {
                 sb.append(line + "\n");
             }
            br.close();

            System.out.println("" + sb.toString());
        } else {
            System.out.println(con.getResponseMessage());
        }




     } catch (MalformedURLException e) {

        e.printStackTrace();
    } catch (IOException e) {

        e.printStackTrace();
    } catch (Exception e) {
        e.printStackTrace();
    } finally {
        if (con != null) {
            con.disconnect();

        }
    }
}

@Override
public void checkClientTrusted(X509Certificate[] arg0, String arg1)
        throws CertificateException {
    // TODO Auto-generated method stub

}

@Override
public void checkServerTrusted(X509Certificate[] arg0, String arg1)
        throws CertificateException {
    // TODO Auto-generated method stub

}

@Override
public X509Certificate[] getAcceptedIssuers() {
    // TODO Auto-generated method stub
    return null;
}
