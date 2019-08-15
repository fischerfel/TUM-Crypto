package stsclientexample3;

import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Map;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;
import javax.xml.ws.Service.Mode;
import javax.xml.ws.soap.AddressingFeature;

import org.oasis_open.docs.ws_sx.ws_trust._200512.RequestSecurityTokenType;

public class STSClientExample3 {

    public static void main(String[] args) {
        try
        {
    final Service service 
      = Service.create( 
              new URL("file://\\project\\VendorDemo\\DocumentAPIv22Methods\\EchoSignDocumentService22Demo\\Java\\STSClientExample2\\web\\WEB-INF\\wsdl\\SecurityTokenService.wsdl"),
                        new QName("http://schemas.microsoft.com/ws/2008/06/identity/securitytokenservice", "SecurityTokenService") 
                       );
    Iterator<QName> ports = service.getPorts();
    while (ports.hasNext()) {
        QName port = ports.next();
        System.out.println(port.toString());
    }

    final Dispatch dispatch 
      = service.createDispatch(new QName("http://schemas.microsoft.com/ws/2008/06/identity/securitytokenservice", "CustomBinding_IWSTrust13Sync"), 
                                JAXBContext.newInstance("org.oasis_open.docs.ws_sx.ws_trust._200512"), 
                                Mode.PAYLOAD, 
                                new AddressingFeature()
                              );

    final BindingProvider provider = (BindingProvider) dispatch;

    final Map requestContext = provider.getRequestContext();

    requestContext.put(BindingProvider.SOAPACTION_URI_PROPERTY, "http://docs.oasis-open.org/ws-sx/ws-trust/200512/RST/Issue");

    final RequestSecurityTokenType request = new RequestSecurityTokenType();

    dispatch.invoke(new JAXBElement<RequestSecurityTokenType>
                          (new QName("http://docs.oasis-open.org/ws-sx/ws-trust/200512", "RequestSecurityToken"), 
                            RequestSecurityTokenType.class, 
                            request)
                          );
        }
         catch (Exception e) { e.printStackTrace(); }
        System.out.println("all done");
    }

    public static void disableCertificates()
    {
        try {
            TrustManager[] trustAllCerts = 
            {new X509TrustManager() 
                {
                    public X509Certificate[] getAcceptedIssuers() { return null; }
                    public void checkClientTrusted(X509Certificate[] certs, String authType) { }
                    public void checkServerTrusted(X509Certificate[] certs, String authType) { }
                }
            };
            SSLContext sc = SSLContext.getInstance("SSL");
            HostnameVerifier hv 
                    = new HostnameVerifier() { public boolean verify(String arg0, SSLSession arg1) { return true; } };
            sc.init(null, trustAllCerts, new SecureRandom());

            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier(hv);
        } 
        catch (Exception localException) 
        {
            System.out.println("Problem disabling SSL certificates: " + localException.getMessage());
        }
    }

}
