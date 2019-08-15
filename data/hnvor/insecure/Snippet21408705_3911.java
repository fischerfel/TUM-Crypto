WebServiceTemplate template = new WebServiceTemplate();
HttpsUrlConnectionMessageSender sender = new HttpsUrlConnectionMessageSender();
sender.setHostnameVerifier(new NullHostnameVerifier());
template.setMessageSender(sender);



public class NullHostnameVerifier implements HostnameVerifier {
    public boolean verify(String hostname, SSLSession session) {
        return true;
    }
}
