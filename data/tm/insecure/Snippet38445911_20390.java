/**
Inner class for set a blind TrustManager
**/
public class BlindTrustManager implements X509TrustManager {
    public X509Certificate[] getAcceptedIssuers() {
        return new X509Certificate[0];
    }
    public void checkClientTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }
    public void checkServerTrusted(X509Certificate[] chain, String authType)
            throws CertificateException {
    }
}

/*Get the port instance of the web service interface created by JAX-WS*/
SolicitarBaixaPortType port = InvoiceWriteOffWebService.getInstance(serviceName, wsdlLocation);

/*set the socket factory*/
SSLContext ctx = SSLContext.getInstance("TLSv1");
            ctx.init(null, new TrustManager[] { new BlindTrustManager() }, null);
            SSLSocketFactory sslSocketFactory = ctx.getSocketFactory();
            ((BindingProvider) port).getRequestContext().put(BindingProviderProperties.SSL_SOCKET_FACTORY, sslSocketFactory);
((BindingProvider) port).getRequestContext().put(com.sun.xml.internal.ws.client.BindingProviderProperties.SSL_SOCKET_FACTORY, sslSocketFactory);

/*message is the Object Jax generated*/
response = port.solicitarBaixaOperation(message);
