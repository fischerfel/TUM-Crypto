import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.KeyStoreSpi;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.X509Certificate;
import java.util.HashMap;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.BasicConfigurator;

//  *removed some import statements here*

public class WebSealAuth {

    private static HttpClient client;
    private static KeyStore keystore;
    private static KeyStore truststore;

    public static void main(String[] args) throws IOException {

        BasicConfigurator.configure();

        try {
            // PKI Card Access
            keystore = KeyStore.getInstance("Windows-MY", "SunMSCAPI");
            keystore.load(null, null);

            // _fixAliases(keystore);

            truststore = KeyStore.getInstance("JKS");
            InputStream truststoreInput = WebSealAuth.class.getResourceAsStream("/caFun42.jks");
            truststore.load(truststoreInput, "PREEVision".toCharArray());

            // System.setProperty("javax.net.ssl.trustStore", "/caFun42.jks");
            // System.setProperty("javax.net.ssl.trustStorePassword", "PREEVision");

            FunctionHierarchyServiceV1 service = new FunctionHierarchyServiceV1();
            HeaderHandlerResolver res = new HeaderHandlerResolver();
            service.setHandlerResolver(res);
            FunctionHierarchyServicePortType portType = service.getFunctionHierarchyServiceSOAPPort();

            String url = null;
            HttpGet httpGet = null;
            url = "https://*removed*/FunctionHierarchyServiceV1";

            httpGet = new HttpGet(url);
            client = getHttpsClient((BindingProvider) portType);

            // Not interested in the response, I just want webseal to ask for my PKI Information
            client.execute(httpGet);

            /* Set NEW Endpoint Location */
            String endpointURL = "https://*removed*FunctionHierarchyServiceV1";
            BindingProvider bp = (BindingProvider) portType;
            bp.getRequestContext().put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY, endpointURL);

            try {
                AliveTestAcknowledgementType aliveStatus = portType.processAliveTest();
                ServiceInfoType abc = aliveStatus.getServiceInfo();
                NameType nameType = abc.getName();
                IdentifierType revisionType = abc.getRevision();
                System.out.println("Revision: " + revisionType.getValue());
                System.out.println("Name: " + nameType.getValue());

            } catch (RespondFunctionHierarchyServiceException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    public static HttpClient getHttpsClient(BindingProvider portType) throws NoSuchAlgorithmException, UnrecoverableKeyException, KeyStoreException,
            KeyManagementException {

        final SSLContext sc = SSLContext.getInstance("TLSv1.2");
        final KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        System.out.println("KMF Algo: " + KeyManagerFactory.getDefaultAlgorithm());

        kmf.init(keystore, null);

        final TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        System.out.println("TMF Algo: " + TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(truststore);

        // X509ExtendedKeyManager manager;
        // manager.chooseClientAlias(arg0, arg1, arg2) ????

        sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), new java.security.SecureRandom());

        SSLConnectionSocketFactory factory = new SSLConnectionSocketFactory(sc, SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        HttpClient client = HttpClients.custom().setSSLSocketFactory(factory).build();

        // portType.getRequestContext().put("com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory",
        // sc.getSocketFactory());

        return client;
    }
