this.algorithm = "hmacSHA256";
private static Mac mac;
String message = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum id urn";
String secretKey = "5771CC06-B86D-41A6-AB39-9CA2BA338E27";

if( mac == null ) {
    mac = Mac.getInstance(algorithm);
    SecretKeySpec secret = new SecretKeySpec(secretKey.getBytes("US-ASCII"), 
        mac.getAlgorithm());
    mac.init(secret);
}
this.signature = new String(Base64.encodeBase64(mac.doFinal(message.getBytes("US-ASCII"))));
