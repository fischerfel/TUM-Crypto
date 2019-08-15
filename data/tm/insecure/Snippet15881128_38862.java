package com.ievgen.webservices;

import java.io.File;

import javax.net.ssl.*;
import javax.xml.ws.BindingProvider;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;

import info.app.catalogservices.schemas.customerquery_1.CustomerQueryType;
import info.app.catalogservices.services.csscatalogservicesinterface_1_0.CustomerError;
import info.app.catalogservices.services.csscatalogservicesinterface_1_0.IntfCatalogServicesService;
import info.app.catalogservices.services.csscatalogservicesinterface_1_0.PortType;

public class TestClient4 {

    /**
     * @param args
     * @throws MalformedURLException
     */
    public static void main(String[] args) throws MalformedURLException {
        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(
                    java.security.cert.X509Certificate[] certs, String authType) {
            }
        } };

        // TODO Auto-generated method stub
        File wsdlFile = new File("WebContent/CatalogServices-1.1.0.wsdl");
        URL wsdlUrl = null;
        if (wsdlFile.exists()) {
            wsdlUrl = wsdlFile.toURL();
        } else {
            System.out.println("File doesn't exist");
        }

        System.out.println("WSDL url: " + wsdlUrl);
        IntfCatalogServicesService service = new IntfCatalogServicesService(
                wsdlUrl);
        PortType portType = service.getPortTypeEndpoint();

        // This will configure the http conduit dynamically
        Client client = ClientProxy.getClient(portType);

        client.getRequestContext().put("ws-security.username", "username");
        client.getRequestContext().put("ws-security.password", "password");

        HTTPConduit http = (HTTPConduit) client.getConduit();

        TLSClientParameters tlsParams = new TLSClientParameters();
        // set trust manager which trusts to all certificates
        tlsParams.setTrustManagers(trustAllCerts);
        // The certificate provides another CN that it really is, so disable
        // CNcheck
        tlsParams.setDisableCNCheck(true);

        http.setTlsClientParameters(tlsParams);

        String resp;
        CustomerQueryType customerQuery = new CustomerQueryType();
        customerQuery.setCustomer("0056712120");
        java.util.Map<String,Object> requestContext = ((BindingProvider) portType)
                .getRequestContext();
            //I set username/password twice because I am not sure where to do it better
            //I have found different directions in different sources
        requestContext.put("ws-security.username", "username");
        requestContext.put("ws-security.password", "password");
        requestContext
                .put(BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
                        "https://host:port/Services/intfCatalogServices-service.serviceagent/portTypeEndpoint");

        try {
            resp = portType.getCustomerOp(customerQuery).toString();
            System.out.println(resp);
        } catch (CustomerError e) {
            e.printStackTrace();
        }
    }

}
