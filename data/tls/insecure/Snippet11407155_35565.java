import javax.net.ssl.*;
import javax.net.SocketFactory;
import java.net.*;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Hashtable;
import java.math.BigInteger;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.protocol.*;


public class HTTPPostDemo {

    private String privateKey;
    private String host;
    private int port;
    private String userName;
    private Header[] headers = null;

    public class MySSLSocketFactory implements SecureProtocolSocketFactory {

        private TrustManager[] getTrustManager() {
            TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                    public void checkClientTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                    public void checkServerTrusted(
                            java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };
            return trustAllCerts;
        }

        public Socket createSocket(String host, int port) throws IOException, UnknownHostException {

            TrustManager[] trustAllCerts = getTrustManager();
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
                return socketFactory.createSocket(host, port);
            } catch (Exception ex) {
                throw new UnknownHostException("Problems to connect " + host + ex.toString());
            }
        }

        public Socket createSocket(Socket socket, String host, int port, boolean flag) throws IOException, UnknownHostException {
            TrustManager[] trustAllCerts = getTrustManager();
            try {

                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
                return socketFactory.createSocket(host, port);
            } catch (Exception ex) {
                throw new UnknownHostException("Problems to connect " + host + ex.toString());
            }

        }

        public Socket createSocket(String host, int port, InetAddress clientHost, int clientPort) throws IOException, UnknownHostException {

            TrustManager[] trustAllCerts = getTrustManager();
            try {
                SSLContext sc = SSLContext.getInstance("SSL");
                sc.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
                SocketFactory socketFactory = HttpsURLConnection.getDefaultSSLSocketFactory();
                return socketFactory.createSocket(host, port, clientHost, clientPort);
            } catch (Exception ex) {
                throw new UnknownHostException("Problems to connect " + host + ex.toString());
            }

        }
    }

    public SslClient(String host, int port, String userName, String privateKey) {
        this.host = host;
        this.port = port;
        this.userName = userName;
        this.privateKey = privateKey;
    }

    protected String md5Sum(String str) {
        String sum = new String();
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            sum = String.format("%032x", new BigInteger(1, md5.digest(str.getBytes())));
        } catch (Exception ex) {
        }
        return sum;

    }

    public String getSignature(String xml) {
        return md5Sum(md5Sum(xml + privateKey) + privateKey);
    }

    public String sendRequest(String xml) throws Exception {

        HttpClient client = new HttpClient();
        client.setConnectionTimeout(60000);
        client.setTimeout(60000);
        String response = new String();
        String portStr = String.valueOf(port);
        Protocol.registerProtocol("https", new Protocol("https", new MySSLSocketFactory(), port));
        String signature = getSignature(xml);
        String uri = "https://" + host + ":" + portStr + "/";
        PostMethod postRequest = new PostMethod(uri);
        postRequest.addRequestHeader("Content-Length", String.valueOf(xml.length()));
        postRequest.addRequestHeader("Content-Type", "text/xml");
        postRequest.addRequestHeader("X-Signature", signature);
        postRequest.addRequestHeader("X-Username", userName);
        postRequest.setRequestBody(xml);
        System.out.println("Sending https request....." + postRequest.toString());

        try {
            client.executeMethod(postRequest);
        } catch (Exception ex) {
            throw new TaskExecuteException("Sending post got exception ", ex);
        }

        response = postRequest.getResponseBodyAsString();
        headers = postRequest.getRequestHeaders();
        return response;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Header[] getHeaders() {
        return headers;
    }

    public void setHeaders(Header[] headers) {
        this.headers = headers;
    }

    public static void main(String[] args) {

        String privateKey = "your_private_key";
        String userName = "your_user_name";
        String host = "demo.site.net";
        int port = 55443;

        String xml =
                "<?xml version='1.0' encoding='UTF-8' standalone='no' ?>"
                + "<!DOCTYPE OPS_envelope SYSTEM 'ops.dtd'>"
                + "<OPS_envelope>"
                + "<header>"
                + "<version>0.9</version>"
                + "<msg_id>2.21765911726198</msg_id>"
                + "<msg_type>standard</msg_type>"
                + "</header>"
                + "<body>"
                + "<data_block>"
                + "<dt_assoc>"
                + "<item key='attributes'>"
                + "<dt_assoc>"
                + "<item key='domain'>test-1061911771844.com</item>"
                + "<item key='pre-reg'>0</item>"
                + "</dt_assoc>"
                + "</item>"
                + "<item key='object'>DOMAIN</item>"
                + "<item key='action'>LOOKUP</item>"
                + "<item key='protocol'>XCP</item>"
                + "</dt_assoc>"
                + "</data_block>"
                + "</body>"
                + "</OPS_envelope>";

        SslClient sslclient = new SslClient(host, port, userName, privateKey);

        try {
            String response = sslclient.sendRequest(xml);
            System.out.println("\nResponse is:\n" + response);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }
}
