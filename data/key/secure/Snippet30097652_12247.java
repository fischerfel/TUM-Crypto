public class HelloWorld{

   public static String encode(String key, String data) throws Exception {
    Base64.Decoder decoder= Base64.getDecoder();
    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(decoder.decode(key), "HmacSHA256");
    sha256_HMAC.init(secret_key);
    byte[] mac_data = sha256_HMAC.doFinal(data.getBytes());
    Base64.Encoder encoder = Base64.getEncoder();
    return encoder.encodeToString(mac_data);
}

 public static void main(String []args){
     try{
    System.out.println(encode("rain", "http://google.com"));
     }
     catch(Exception e)
     {

     }
 }
}
