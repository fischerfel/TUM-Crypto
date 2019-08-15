public class TwitCons {

    String sign_method ="HMAC-SHA1";

    public static String consumer_key ="^^^^^^^^^^^^^^^^^^^^";
    public static String consumer_secret="^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^";
    String request_uri = "https://api.twitter.com/oauth/request_token";
    String oauth_callback="http://127.0.0.1:8080/twitter/getToken";

    String getNonce(){
        Random ran = new Random(); 
        long nonce1 = ran.nextLong(); 

        Long nonce = Math.abs(new Long(nonce1)); 

        return nonce.toString();
    }


    String getTimestamp(){
        long time = System.currentTimeMillis();


        return String.valueOf(time/1000);
    }


    String getSignature(String request_uri ,String request_token,String nonce) throws UnsupportedEncodingException
    {
        String base="oauth_callback=http://127.0.0.1:8080/twitter/getToken&oauth_consumer_key="+consumer_key+"&oauth_nonce="+nonce+"&oauth_signature_method="+sign_method+"&oauth_timestamp="+getTimestamp()+"&oauth_token=&oauth_version=1.0";

        String baseString="POST&"+URLEncoder.encode(request_uri , "UTF-8")+"&"+URLEncoder.encode(base , "UTF-8");

        String signKey = consumer_secret +"&"+ request_token ;
        System.out.println(signKey +"\n"+baseString);

        try {
            SecretKeySpec signingKey = new SecretKeySpec(signKey.getBytes(),"HmacSHA1");
            Mac mac = Mac.getInstance(signingKey.getAlgorithm());
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(baseString.getBytes());
            String result = new String(Base64.encodeBase64(rawHmac));
            System.out.println(URLEncoder.encode(result , "UTF-8"));
            return URLEncoder.encode(result , "UTF-8");
            //return result;

        } catch (GeneralSecurityException e) {
            return "";
        }




    }


}
