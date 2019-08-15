import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.security.cert.CertificateException;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.cert.X509Certificate;
import javax.xml.ws.Holder;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import com.microsoft.schemas.sharepoint.soap.copy.CopyResultCollection;
import com.microsoft.schemas.sharepoint.soap.copy.CopySoap;
import com.microsoft.schemas.sharepoint.soap.copy.DestinationUrlCollection;
import com.microsoft.schemas.sharepoint.soap.copy.FieldInformation;
import com.microsoft.schemas.sharepoint.soap.copy.FieldInformationCollection;
import com.microsoft.schemas.sharepoint.soap.copy.FieldType;

public class Upload {

    private static String username = "yourusrename";

    private static String password = "yourpassword";

    private static String targetPath = "https://www.yoursite.target/filename";

    private static String sourcePath = "file.txt";

    private static String portUrl = "https://www.yoursite.com/_vti_bin/Copy.asmx";

    private static CopySoap soapInstance;

    public static void main(String[] args) {
        activate();
        CopySoap sqs = getInstance();
        String url = targetPath;
        String sourceUrl = sourcePath;
        DestinationUrlCollection urls = new DestinationUrlCollection();
        urls.getString().add(url);
        File file = null;
        byte[] content = null;
        try {
            FileInputStream fileStream = new FileInputStream(file = new File(sourceUrl));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[1024];

            for (int readNum; (readNum = fileStream.read(buf)) != -1;) {
                bos.write(buf, 0, readNum);
            }

            content = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        FieldInformation titleInfo = new FieldInformation();
        titleInfo.setDisplayName("testpage");
        titleInfo.setType(FieldType.TEXT);
        titleInfo.setValue("Test Page");
        FieldInformationCollection infos = new FieldInformationCollection();
        infos.getFieldInformation().add(titleInfo);
        CopyResultCollection results = new CopyResultCollection();
        Holder<CopyResultCollection> resultHolder = new Holder<CopyResultCollection>(results);
        Holder<Long> longHolder = new Holder<Long>(new Long(-1));
        if (content != null) {
            sqs.copyIntoItems(sourceUrl, urls, infos, content, longHolder, resultHolder);
        }
    }

    private static void activate() {

        JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(CopySoap.class);
        factory.setAddress(portUrl);
        factory.getInInterceptors().add(new LoggingInInterceptor());
        factory.getOutInterceptors().add(new LoggingOutInterceptor());
        soapInstance = (CopySoap) factory.create();
        Authenticator.setDefault(new SPAuthenticator());
        Client client = ClientProxy.getClient(soapInstance);
        HTTPConduit http = (HTTPConduit) client.getConduit();
        HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(10000);
        httpClientPolicy.setAllowChunking(false);
        HTTPConduit conduit = (HTTPConduit) client.getConduit();
        conduit.setClient(httpClientPolicy);
        TLSClientParameters tcp = new TLSClientParameters();
        tcp.setTrustManagers(new TrustManager[] { (TrustManager) new TrustAllX509TrustManager() });
        conduit.setTlsClientParameters(tcp);
    }

    public static CopySoap getInstance() {
        return soapInstance;
    }

    static class SPAuthenticator extends Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            System.out.println("hitting SP with username and password for " + getRequestingScheme());
            return (new PasswordAuthentication(username, password.toCharArray()));
        }
    }

    /**
     * This class allow any X509 certificates to be used to authenticate the
     * remote side of a secure socket, including self-signed certificates.
     */
    public static class TrustAllX509TrustManager implements X509TrustManager {

        /** Empty array of certificate authority certificates. */
        private static final X509Certificate[] acceptedIssuers = new X509Certificate[] {};

        /**
         * Always trust for client SSL chain peer certificate chain with any
         * authType authentication types.
         * 
         * @param chain
         *            the peer certificate chain.
         * @param authType`enter
         *            code here` the authentication type based on the client
         *            certificate.
         */
        public void checkClientTrusted(X509Certificate[] chain, String authType) {
        }

        /**
         * Always trust for server SSL chain peer certificate chain with any
         * authType exchange algorithm types.
         * 
         * @param chain
         *            the peer certificate chain.
         * @param authType
         *            the key exchange algorithm used.
         */
        public void checkServerTrusted(X509Certificate[] chain, String authType) {
        }

        /**
         * Return an empty array of certificate authority certificates which are
         * trusted for authenticating peers.
         * 
         * @return a empty array of issuer certificates.
         */
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        @Override
        public void checkClientTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }

        @Override
        public void checkServerTrusted(java.security.cert.X509Certificate[] arg0, String arg1)
                throws CertificateException {
            // TODO Auto-generated method stub

        }
    }
}
