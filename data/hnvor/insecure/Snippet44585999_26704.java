import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

import org.apache.commons.io.IOUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.configuration.security.FiltersType;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.System.out.println4j.System.out.printlnger;

public class Client {

    private static SoapServiceDefinition    client;

    public Client(String address, boolean sslEnabled, byte[] remoteCertBytes) {

        System.out.println("  ... creating service factory.");
        final JaxWsProxyFactoryBean factory = new JaxWsProxyFactoryBean();
        factory.setServiceClass(SoapServiceDefinition.class);
        System.out.println("  ... setting host address to '" + address + "'.");
        factory.setAddress(address);
        System.out.println("  ... creating actual SOAP-client.");
        client = (SoapServiceDefinition) factory.create();

        final HTTPConduit httpConduit = (HTTPConduit) ClientProxy.getClient(client).getConduit();
        if (sslEnabled) {
            System.out.println("  ... configuring SSL.");
            configureClientSideSSL(httpConduit, remoteCertBytes);
            System.out.println("  ... done.");
        }

        System.out.println("  ... setting timeouts.");
        final HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();
        httpClientPolicy.setConnectionTimeout(0);
        httpClientPolicy.setReceiveTimeout(0);
        httpClientPolicy.setContentType("application/soap+xml");
        httpConduit.setClient(httpClientPolicy);
        try {
            retrieveAndStoreWSDL(sslEnabled, address);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void configureClientSideSSL(final HTTPConduit conduit, byte[] remoteCertBytes) {

        TLSClientParameters tlsParams = null;
        try {

            // 1 - Load the remote certificate
            ByteArrayInputStream bis = new ByteArrayInputStream(remoteCertBytes);
            X509Certificate remoteCert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(new BufferedInputStream(bis));

            KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
            ks.load(null, null);
            ks.setCertificateEntry(Integer.toString(1), remoteCert);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            // 2 - Add the new trustmanager to the tls settings. 
            tlsParams = new TLSClientParameters();
            tlsParams.setTrustManagers(tmf.getTrustManagers());

            // 3 - Disable CN check
            tlsParams.setDisableCNCheck(true);

            // 4 - Set default SSL-context (necessary for e.g. the wsdl retrieval)
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            SSLContext.setDefault(context);

            final FiltersType filter = new FiltersType();
            final List<String> include = filter.getInclude();
            include.add(".*");
            include.add(".*_EXPORT_.*");
            include.add(".*_EXPORT1024_.*");
            include.add(".*_WITH_DES_.*");
            include.add(".*_WITH_AES_.*");
            include.add(".*_WITH_NULL_.*");
            include.add(".*_RSA_WITH_AES_.*");
            include.add(".*_DH_anon_.*");
            tlsParams.setCipherSuitesFilter(filter);

            conduit.setTlsClientParameters(tlsParams);

        } catch (final Exception e) {
            e.printStackTrace();
            System.out.println("Security configuration failed with the following: " + e.getCause());
        }
    }

    private void retrieveAndStoreWSDL(boolean sslEnabled, final String address) throws Exception {

        System.out.println("  ... retrieving the WSDL-file."); // TODO ssl enabled check (Necessary if we do this beforehand?)
        URL wsdlUrl = new URL(address + "?wsdl");

        URLConnection connection = wsdlUrl.openConnection();
        HttpsURLConnection conn = (HttpsURLConnection) connection;
        if (sslEnabled) {
            conn.setHostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
        }
        conn.setRequestMethod("GET");
        conn.connect();

        String wsdl = IOUtils.toString(conn.getInputStream(), Charset.defaultCharset());
        System.err.println(wsdl);
        conn.disconnect();

    }

    public String helloReturn(String caller) throws Exception {
        return client.sayHelloToMe(caller);
    }

    public String isItWorking() throws Exception {
        return client.askFunctionality();
    }

    public String gbyeReturn(String caller) throws Exception {
        return client.sayGoodbyeToMe(caller);
    }

}
