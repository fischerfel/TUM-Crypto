package com.b5team.postrequest;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class HttpsPOSTRequest {

    public static void sendRequest(String myurl, String hash, String args[]) throws NoSuchAlgorithmException, KeyManagementException {

        try {
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(new KeyManager[0], new TrustManager[] {new DefaultTrustManager()}, new SecureRandom());
            SSLContext.setDefault(context);

            URL url = new URL(myurl);
            HttpsURLConnection con = (HttpsURLConnection)url.openConnection();
            con.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            });

            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)");
            con.setDoOutput(true);
            con.setDoInput(true);

            ArrayList<String> params = new ArrayList<String>(args.length + 1);

            DataOutputStream output = new DataOutputStream(con.getOutputStream());
            output.writeBytes("hash=" + hash);
            for(int i = 0; i < params.size(); i++) {
                output.writeBytes("&");
                output.writeBytes("arg" + i + "=" + args[i]);
                output.flush();
            }

            output.flush();
            output.close();

            DataInputStream input = new DataInputStream(con.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            String line;

            System.out.println("[POSTRequest] Data sent successfully!");
            System.out.println("[POSTRequest] Resp Code:"+con.getResponseCode());
            System.out.println("[POSTRequest] Resp Message:"+con.getResponseMessage());

            while ((line = reader.readLine()) != null) {
                System.out.println("[POSTRequest] Report: "+line);
            }

            input.close();

        } catch (UnsupportedEncodingException e) {
            System.out.println("[POSTRequest] Encoding error. Maybe string have invalid caracters.");
            e.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("[POSTRequest] Invalid URL. Verify your URL and try again.");
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("[POSTRequest] Error on HTTPS connection.");
            e.printStackTrace();
        }
    }

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
}
