import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebEndpoint;
import javax.xml.ws.WebServiceClient;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.WebServiceFeature;

@WebServiceClient(name = "ExternalWSImplService", targetNamespace = "http://service.ws.padron.ingartek.com/", wsdlLocation = "URL of WSDL")
public class ExternalWSImplService extends Service {

    private final static URL EXTERNALWSIMPLSERVICE_WSDL_LOCATION;
    private final static WebServiceException EXTERNALWSIMPLSERVICE_EXCEPTION;
    private final static QName EXTERNALWSIMPLSERVICE_QNAME = new QName("http://service.ws.padron.ingartek.com/", "ExternalWSImplService");

    TrustManager[] trustAllCerts = new TrustManager[] {
       new X509TrustManager() {
          public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          public void checkClientTrusted(X509Certificate[] certs, String authType) {  }

          public void checkServerTrusted(X509Certificate[] certs, String authType) {  }

       }
    };

    static {
        URL url = null;
        WebServiceException e = null;

        try {
            url = new URL(/*https URL*/);
        } catch (MalformedURLException ex) {
            e = new WebServiceException(ex);
        }
        EXTERNALWSIMPLSERVICE_WSDL_LOCATION = url;
        EXTERNALWSIMPLSERVICE_EXCEPTION = e;
    }

    public ExternalWSImplService() {
        super(__getWsdlLocation(), EXTERNALWSIMPLSERVICE_QNAME);
    }

    public ExternalWSImplService(WebServiceFeature... features) {
        super(__getWsdlLocation(), EXTERNALWSIMPLSERVICE_QNAME, features);
    }

    public ExternalWSImplService(URL wsdlLocation) {
        super(wsdlLocation, EXTERNALWSIMPLSERVICE_QNAME);
    }

    public ExternalWSImplService(URL wsdlLocation, WebServiceFeature... features) {
        super(wsdlLocation, EXTERNALWSIMPLSERVICE_QNAME, features);
    }

    public ExternalWSImplService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    public ExternalWSImplService(URL wsdlLocation, QName serviceName, WebServiceFeature... features) {
        super(wsdlLocation, serviceName, features);
    }

    @WebEndpoint(name = "ExternalWSImplPort")
    public ExternalWS getExternalWSImplPort() {

        SSLContext sc;

        try {

        sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        // Create all-trusting host name verifier
        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
              return true;
            }
        };
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);

        } catch (NoSuchAlgorithmException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        } catch (KeyManagementException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        return super.getPort(new QName("http://service.ws.padron.ingartek.com/", "ExternalWSImplPort"), ExternalWS.class);
    }

    @WebEndpoint(name = "ExternalWSImplPort")
    public ExternalWS getExternalWSImplPort(WebServiceFeature... features) {
        return super.getPort(new QName("http://service.ws.padron.ingartek.com/", "ExternalWSImplPort"), ExternalWS.class, features);
    }

    private static URL __getWsdlLocation() {
        if (EXTERNALWSIMPLSERVICE_EXCEPTION!= null) {
            throw EXTERNALWSIMPLSERVICE_EXCEPTION;
        }
        return EXTERNALWSIMPLSERVICE_WSDL_LOCATION;
    }

}
