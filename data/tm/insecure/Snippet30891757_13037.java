package unsecureBackup;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.codec.binary.Base64;

public class SendSomaCommand {
    //Code to Allow Opening insecure HTTPS Connection
    //Allowing all DataPower XML Management Interface Cert to create Connection without it's validation
    static{
        try{
            TrustManager[] trustAllCerts = { new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                public void checkClientTrusted(X509Certificate[] certs,
                        String authType) {
                }

                public void checkServerTrusted(X509Certificate[] certs,
                        String authType) {
                }
            } };
            SSLContext sc = SSLContext.getInstance("SSL");

            HostnameVerifier hv = new HostnameVerifier() {
                public boolean verify(String arg0, SSLSession arg1) {
                    return true;
                }
            };
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        }catch(Exception exception){
            System.err.println(exception);
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws Exception{
        //Url - https://dpdev02.dev.anz:5550/service/mgmt/current

        //SomaRequest.xml contains Soma XML Request (See the other attachment)      
        String output = sendRequest("https://IP:5550/service/mgmt/current", "getfile.xml", "Username", "Password");
        System.out.println(output);
    }


    /**
     * Send GetFileStore Request with location "local:" to DataPower box to file local file system
     * @param pUrl
     * @param pXmlFile2Send
     * @param pDomain
     * @param pUsername
     * @param pPassword
     * @return
     * @throws Exception
     * 
     */
    public static String sendRequest(String pUrl, String pXmlFile2Send, String pUsername, String pPassword) throws Exception {
        String SOAPUrl      = pUrl;
        String xmlFile2Send = pXmlFile2Send;
        String SOAPAction = "";


        // Create the connection where we're going to send the file.
        URL url = new URL(SOAPUrl);
        URLConnection connection = url.openConnection();
        HttpsURLConnection httpConn = (HttpsURLConnection) connection;

        // Open the input file. After we copy it to a byte array, we can see
        // how big it is so that we can set the HTTP Content-Length
        // property. (See complete e-mail below for more on this.)
        FileInputStream fin = new FileInputStream(xmlFile2Send);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        // Copy the SOAP file to the open connection.
        copy(fin,bout);
        fin.close();

        //Replace domainName in Request
        String soapRequest = bout.toString();

        //Convert into bytes
        byte[] b = soapRequest.getBytes();

        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty( "Content-Length",String.valueOf( b.length ) );
        httpConn.setRequestProperty("Content-Type","text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction",SOAPAction);

        //Create UsernamePassword 
        //To Base64 decoding, Apache common-codec is used.
        String authString = pUsername + ":" + pPassword;
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        String authStringEnc = new String(authEncBytes);
        httpConn.setRequestProperty("Authorization", "Basic " + authStringEnc);

        //httpConn.setRequestProperty("Authorization","Basic Z295YWxyYWRtaW46VHJhbnNmZXIxMiM=");
        httpConn.setRequestMethod( "POST" );
        httpConn.setDoOutput(true);
        httpConn.setDoInput(true);

        // Everything's set up; send the XML that was read in to b.
        OutputStream out = httpConn.getOutputStream();
        out.write(b);    
        out.close();

        // Read the response and write it to standard out.
        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);

        String inputLine;
        String output = "";
        while ((inputLine = in.readLine()) != null){
            output = output+inputLine;
        }

        in.close();
        return output;
    }

    // copy method from From E.R. Harold's book "Java I/O"
    public static void copy(InputStream in, OutputStream out) 
            throws IOException {

        // do not allow other threads to read from the
        // input or write to the output while copying is
        // taking place
        synchronized (in) {
            synchronized (out) {

                byte[] buffer = new byte[256];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1) break;
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }
}
