package com.cs;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Collection;
import java.util.Iterator;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import net.my.myHostnameVerifier;
import net.my.myX509TrustManager;
import android.app.Activity;
import android.os.Bundle;

public class ClientServerActivity extends Activity {
    protected static int debug = 1;
    String soapRequest="";
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        soapRequest = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:nam=\"https://ind-etsdev/namespace\">"
                + "<soapenv:Header/>"
                + "<soapenv:Body>"
                + "<nam:CreateSession>"
                + "<DeviceID>something</DeviceID>"
                + "<UserName>something</UserName>"
                + "<UserPassword>something</UserPassword>"
                + "</nam:CreateSession>"
                + "</soapenv:Body>"
                + "</soapenv:Envelope>";
        runService();
    }
    private void runService(){
        try {
            debug = 1;
            String certPassword = "my1234";
            initializeURLConn(certPassword, "BKS", null);
            String urlStr = "https://webserviceurl";
            int requestStatus = -1;
            requestStatus = doHttpRequest(urlStr);
            System.out.println("Request Status: " + requestStatus);
        }
        catch(Throwable x) {
            x.printStackTrace();
        }
    }
    private int doHttpRequest(String urlStr) throws Exception {
        String responseBody = "";

        //Open the HTTP connection
        URL url = new URL(urlStr);
        HttpsURLConnection urlconnection = (HttpsURLConnection)url.openConnection();
        urlconnection.setHostnameVerifier(new myHostnameVerifier(true, debug));
        urlconnection.setDoOutput(true);
        urlconnection.setDoInput(true);
        urlconnection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8"); 

        urlconnection.setRequestProperty("Content-Length", Integer.toString(soapRequest.length())); 
        urlconnection.setRequestProperty("SOAPAction", "https://soapaction"); 

        urlconnection.setUseCaches(false);
        urlconnection.setConnectTimeout(30000); //30 seconds
        urlconnection.setReadTimeout(180000); //3 minutes
        urlconnection.connect();

        // Send Request to the server 
        try {
            DataOutputStream outStr = new DataOutputStream(urlconnection.getOutputStream ());   
            outStr.writeBytes(soapRequest); 
            outStr.flush(); 
            outStr.close();
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } 

        //Read the response
        int responseCode = urlconnection.getResponseCode();

        InputStreamReader responseISR = null;
        BufferedReader responseBR = null;

        try {
            responseISR = new InputStreamReader(urlconnection.getInputStream());
        }
        catch (IOException e) {
            System.out.println("IOException!!!!");
            e.printStackTrace();
            responseISR = new InputStreamReader(urlconnection.getErrorStream());
        }

        responseBR = new BufferedReader(responseISR);
        StringBuffer stringbuffer = new StringBuffer();
        String responseLine;
        while ((responseLine = responseBR.readLine()) != null) {
            stringbuffer.append(responseLine + "\n");
        }
        responseBR.close();
        urlconnection.disconnect();

        responseBody = stringbuffer.toString();

        System.out.println(responseBody);

        return responseCode;
    }


    private void initializeURLConn(String keystorePassword, String keystoreType, String trustFileName) throws Exception {
        // Create keystore
        KeyStore keystore = KeyStore.getInstance(keystoreType);
        InputStream keystoreFIS = getResources().openRawResource(R.raw.clientvik);
        keystore.load(keystoreFIS, keystorePassword != null ? keystorePassword.toCharArray(): null);

        // Create key manager
        KeyManagerFactory kmfactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmfactory.init(keystore, keystorePassword != null ? keystorePassword.toCharArray(): null);
        KeyManager[] keymanagers = kmfactory.getKeyManagers();

        TrustManager[] trustmanagers = null;
        if (trustFileName == null) {
            // Create a trust manager that does not validate certificate chains
            trustmanagers = new TrustManager[]{new myX509TrustManager()};
        }
        else {
            // Create trust keystore
            KeyStore ksCA = java.security.KeyStore.getInstance("JKS");
            ksCA.load(null, null);

            // Add MRTU cert to the keystore as a trusted certificate
            FileInputStream truststoreFIS = new FileInputStream(trustFileName);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            Collection c = cf.generateCertificates(truststoreFIS);
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Certificate cert = (Certificate)i.next();
                //System.out.println(cert);
                ksCA.setCertificateEntry("trustedCA", cert);
            }

            // Create trust manager
            TrustManagerFactory tmf = TrustManagerFactory.getInstance("X.509");
            tmf.init(ksCA);
            trustmanagers = (TrustManager[])tmf.getTrustManagers();
        }

        // Install the key manager and trust manager
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(keymanagers, trustmanagers, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }

}
