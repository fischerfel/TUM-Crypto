public class Main{
public static void main(String[]args){
    String compID="compID";
    String consumerKey="consumerKey";
    String consumerSecret="consumerSecret";
    String tokenId="tokenId";
    String tokenSecret="tokenSecret";
    String restletURL="https://rest.sandbox.netsuite.com/app/site/hosting/restlet.nl?script=123&deploy=2";

    Token token=new Token(tokenId,tokenSecret);
    String nonce=RandomStringUtils.randomNumeric(9);
    long ms=new java.util.Date().getTime();
    ms=(long)Math.floor(ms/1000);
    String time=ms+"";

    String baseString=compID+"&"+consumerKey+"&"+tokenId+"&"+nonce+"&"+time;
    String key=consumerSecret+"&"+tokenSecret;
    byte[] bytes=key.getBytes();
    SecretKeySpec secretkey=new SecretKeySpec(bytes,"HmacSHA1");
    Mac keymac=null;
    try{
        keymac=Mac.getInstance("HmacSHA1");
        keymac.init(secretkey);
    }catch(NoSuchAlgorithmException e){
        e.printStackTrace();
    }catch(InvalidKeyException e){
        e.printStackTrace();
    }
    byte[] hash=keymac.doFinal(baseString.getBytes());
    String result=new String(Base64.encodeBase64(hash,false));
    String oauth_timestamp = Instant.now().toEpochMilli()+"";

    String headerAuthorization="OAuth realm=\""+compID+"\", oauth_consumer_key=\""+consumerKey+"\", oauth_token=\""+token+"\", oauth_nonce=\""+nonce+"\", oauth_timestamp=\""+oauth_timestamp+"\", oauth_signature_method=\"HMAC-SHA1\", oauth_version=\"1.0\", oauth_signature=\""+result+"\"";

    System.out.println(headerAuthorization);

    HttpClient httpclient=HttpClientBuilder.create().build();

    HttpGet request=new HttpGet(restletURL);
    request.setHeader(HttpHeaders.AUTHORIZATION,headerAuthorization);

    String response="";
    try{
        //Handle the response from NetSuite
        ResponseHandler<String> responseHandler=new ResponseHandler<String>(){
            @Override
            public String handleResponse(final HttpResponse response)throws ClientProtocolException,IOException{
                int status=response.getStatusLine().getStatusCode();

                //If the call is successful
                if(status>=200 && status<300){
                    HttpEntity entity=response.getEntity();
                    return entity!=null?EntityUtils.toString(entity):null;
                }else{
                    throw new ClientProtocolException("Unexpected response status: "+status);
                }
            }
        };
        response=httpclient.execute(request,responseHandler);
        System.out.println(response);
    }catch(Exception e){
        e.printStackTrace();
    }
  }
}
