import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

public class Main{
    public static void sendSoapRequest() throws Exception {
        String SOAPUrl = "URL HERE";
        String xmlFile2Send = ".\\src\\request.xml";
        String responseFileName = ".\\src\\response.xml";
        String inputLine;

        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
            public void checkClientTrusted(X509Certificate[] certs, String authType) { }
            public void checkServerTrusted(X509Certificate[] certs, String authType) { }

        } };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) { return true; }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        // Create the connection with http
        URL url = new URL(SOAPUrl);
        URLConnection connection = url.openConnection();
        HttpURLConnection httpConn = (HttpURLConnection) connection;
        FileInputStream fin = new FileInputStream(xmlFile2Send);
        ByteArrayOutputStream bout = new ByteArrayOutputStream();

        copy(fin, bout);
        fin.close();

        byte[] b = bout.toByteArray();
        StringBuffer buf=new StringBuffer();
        String s=new String(b);

        b=s.getBytes();

        // Set the appropriate HTTP parameters.
        httpConn.setRequestProperty("Content-Length", String.valueOf(b.length));
        httpConn.setRequestProperty("Content-Type", "text/xml; charset=utf-8");
        httpConn.setRequestProperty("SOAPAction", "");
        httpConn.setRequestMethod("POST");
        httpConn.setDoOutput(true);

        OutputStream out = httpConn.getOutputStream();
        out.write(b);
        out.close();

        // Read the response.
        httpConn.connect();
        System.out.println("http connection status :"+ httpConn.getResponseMessage());
        InputStreamReader isr = new InputStreamReader(httpConn.getInputStream());
        BufferedReader in = new BufferedReader(isr);

        while ((inputLine = in.readLine()) != null)
            System.out.println(inputLine);
        FileOutputStream fos=new FileOutputStream(responseFileName);
        copy(httpConn.getInputStream(),fos);
        in.close();
    }

    public static void copy(InputStream in, OutputStream out) throws IOException {

        synchronized (in) {
            synchronized (out) {
                byte[] buffer = new byte[256];
                while (true) {
                    int bytesRead = in.read(buffer);
                    if (bytesRead == -1)
                        break;
                    out.write(buffer, 0, bytesRead);
                }
            }
        }
    }

    public static void main(String args[]) throws Exception {
        sendSoapRequest();
    }
}
