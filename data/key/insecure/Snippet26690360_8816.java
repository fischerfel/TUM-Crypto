public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeyException 
{
  String password = "password123";
  SecretKeySpec keySpec = new SecretKeySpec(password.getBytes(), "HmacSHA1");

  Mac mac = Mac.getInstance("HmacSHA1");
  mac.init(keySpec);

  byte[] result = mac.doFinal("This is a test string".getBytes());

  System.out.println(new Base64().encodeAsString(result));
}
