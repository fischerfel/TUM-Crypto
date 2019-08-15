package foo.bar;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import javax.net.ssl.*;
import java.io.*;
import java.net.URL;
import java.security.KeyStore;

public class ReadTimedOutTest {

    @Test
    public void testReadTimedOut() throws Exception {
        URL url = new URL("https://services/endpoint/");

        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        setUpSSL(connection);
        connection.setDoOutput(true);
        connection.setDoInput(true);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("SOAPAction", "http://namespace/2012/01/service/Operation");
        connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Content-Type", "text/xml; charset=utf-8");

        connection.setConnectTimeout(10 * 1000);
        connection.setReadTimeout(10 * 1000);
        connection.setInstanceFollowRedirects(true);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        InputStream is = ReadTimedOutTest.class.getResourceAsStream("payload.failure.xml");
        try {
            IOUtils.copy(is, bos);
        } finally {
            is.close();
        }
        byte[] bytes = bos.toByteArray();
        connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));

        OutputStream os = connection.getOutputStream();
        try {
            IOUtils.copy(new ByteArrayInputStream(bytes), os);
            os.flush();
        } finally {
            os.close();
        }

        int respCode = connection.getResponseCode();
        if(respCode >= HttpsURLConnection.HTTP_INTERNAL_ERROR) {
            is = connection.getErrorStream();
            try {
                IOUtils.copy(is, System.err);
            } finally {
                is.close();
            }
        } else {
            is = connection.getInputStream();
            try {
                IOUtils.copy(is, System.out);
            } finally {
                is.close();
            }
        }
    }

    private void setUpSSL(HttpsURLConnection connection) throws Exception {
        byte[] bytes = FileUtils.readFileToByteArray(new File("d:\\workspace\\temp\\keystore"));
        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new ByteArrayInputStream(bytes), "changeit".toCharArray());
        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
        keyManagerFactory.init(keyStore, "changeit".toCharArray());

        bytes = FileUtils.readFileToByteArray(new File("d:\\workspace\\temp\\truststore"));
        KeyStore trustStore = KeyStore.getInstance("JKS");
        trustStore.load(new ByteArrayInputStream(bytes), "changeit".toCharArray());
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("X509");
        trustManagerFactory.init(trustStore);

        SSLContext context = SSLContext.getInstance("TLS");
        context.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
        SSLSocketFactory socketFactory = context.getSocketFactory();

        connection.setSSLSocketFactory(socketFactory);
    }

}
