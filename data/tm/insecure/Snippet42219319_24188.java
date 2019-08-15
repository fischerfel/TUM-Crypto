import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Math.log;
import static java.lang.System.out;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONObject;

import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;




public String reqSender()() throws Exception {
        String acessToken = null;
        String rfshToken = null;
        double expiresIn = 0.0D;
        int x = 0;
        SSLCert.trustAllHostnames();
        SSLCert.trustAllHttpsCertificates();
        //DbCon con = new DbCon();

        try {
            String refreshToken = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
            System.out.println(refreshToken);
            System.out.println("");
            System.out.println("Calling web service...");

            URL url = new URL(
                    "https://mifxx.xxxxx.lk/apicall/token?grant_type=refresh_token&refresh_token="
                    + refreshToken + "&scope=PRODUCTION");
            System.out.println("web service called" + url.toString());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            conn.setRequestProperty(
                    "Authorization",
                    "Basic xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx1JndXc3bU1h");
    enter code here
            /////start
            HttpsURLConnection conHttps = (HttpsURLConnection) conn;
            TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {

                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }

                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] certs, String authType) {
                }
            }};

            HostnameVerifier allHostsValid = new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            };

            // Install the all-trusting trust manager
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc
                        .getSocketFactory());
                HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
                conn = conHttps;
            } catch (Exception e) {
            }

            /////End
            x = conn.getResponseCode();
            System.out.println(conn.getResponseCode() + "CODE");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("Failed : HTTP error code : "
                        + conn.getResponseCode());
            }

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    conn.getInputStream()));

            System.out.println("Output from Server ....");
            String output = null;
            while ((output = br.readLine()) != null) {
                System.out.println(output);

                JSONObject obj = new JSONObject(output);
                String defult = obj.getString("scope");
                String tokenType = obj.getString("token_type");
                expiresIn = obj.getDouble("expires_in");
                rfshToken = obj.getString("refresh_token");
                acessToken = obj.getString("access_token");

                System.out.println("");
                System.out.println("-------------------------");
                System.out.println("Access Token :" + acessToken);
                System.out.println("Refresh Token :" + rfshToken);
                System.out.println("Token Type :" + tokenType);
                System.out.println("Scope :" + defult);
                System.out.println("Expiration time: " + expiresIn);
                System.out.println("-------------------------");
                System.out.println("");
            }

            conn.disconnect();
        } catch (MalformedURLException e) {
            //log.error(e);
            e.printStackTrace();
        } catch (IOException e) {
            //log.error(e);
            e.printStackTrace();
        }
        return rfshToken;
    }
