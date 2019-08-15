    public class CmsClient  extends WebServiceGatewaySupport{
    public GetAccountResponse getAccountById(String accountId) {
    GetAccount request = new GetAccount();
    request.setSubscriberId(null);

    request.setAccountId("11111");



    WebServiceTemplate template = getWebServiceTemplate();
    HttpsUrlConnectionMessageSender sender = new HttpsUrlConnectionMessageSender();
    sender.setTrustManagers(new TrustManager[] { new UnTrustworthyTrustManager() });
    sender.setHostnameVerifier(new NullHostnameVerifier());

    template.setMessageSender(sender);
    GetAccountResponse response = (GetAccountResponse) template.marshalSendAndReceive(
            request, new WebServiceMessageCallback(){
                public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException{
                    SaajSoapMessage soapMessage = (SaajSoapMessage) message;
                    SoapHeaderElement header =  soapMessage.getSoapHeader().addHeaderElement(new QName("http://www.example.com/functions/api/common", "header", "com"));
                }
            });


    return response;
}   

}
