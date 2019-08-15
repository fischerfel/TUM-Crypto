Mac mac = Mac.getInstance("HmacSha256");
SecretKeySpec secret = new SecretKeySpec(key.getBytes(), "HmacSha256");
mac.init(secret);
byte[] shaDigest = mac.doFinal(phrase.getBytes());
String hash = "";
for(byte b:shaDigest) {
    hash += String.format("%02x",b);
}
