protected String hashToString(String serializedModel, byte[] key) {

String result = null;

Mac sha512_HMAC;

try {

  sha512_HMAC = Mac.getInstance("HmacSHA512");      

  SecretKeySpec secretkey = new SecretKeySpec(key, "HmacSHA512");

  sha512_HMAC.init(secretkey);

   byte[] mac_data = sha512_HMAC.doFinal(serializedModel.getBytes("UTF-8"));        

   result = Base64.encodeBase64String(mac_data);

}catch(Exception e){
}
}
