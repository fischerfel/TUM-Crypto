public String calcHmac(String src) throws Exception {

    String key = "d6fc3a4a06ed55d24fecde188aaa9161";
    Mac mac = Mac.getInstance("HmacSHA1");
    SecretKeySpec sk = new SecretKeySpec(key.getBytes(),mac.getAlgorithm());  
    mac.init(sk);
    byte[] result = mac.doFinal(src.getBytes());


    return Base64.encodeToString(result ,Base64.URL_SAFE);
}
