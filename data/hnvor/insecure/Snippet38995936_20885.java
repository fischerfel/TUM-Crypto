public void setWebServicesTemplate(WebServicesTemplate template) {
   HostnameVerifier verifier = new NullHostnameVerifier();
   HttpsUrlConnectionMessageSender sender = new HttpsUrlConnectionMessageSender();
   sender.setHostnameVerifier(verifier);
   template.setMessageSender(sender);
   this.template = template;
}
public class NullHostnameVerifier implements HostnameVerifier {
   public boolean verify(String hostname, SSLSession session) {
      return true;
   }
}
