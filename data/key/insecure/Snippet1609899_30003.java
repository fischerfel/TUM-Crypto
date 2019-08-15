String mykey = "secret";
String test = "test";
try {
    Mac mac = Mac.getInstance("HmacSHA1");
    SecretKeySpec secret = new SecretKeySpec(mykey.getBytes(),"HmacSHA1");
    mac.init(secret);
    byte[] digest = mac.doFinal(test.getBytes());
    String enc = new String(digest);
    System.out.println(enc);  
} catch (Exception e) {
    System.out.println(e.getMessage());
}
