String genChallengeResponse(String challenge, String message) {
  String Hmac_ALG = "HmacSHA256";
  SecretKey key = new SecretKeySpec(challenge.getBytes(), Hmac_ALG);
  Mac m = Mac.getInstance(Hmac_ALG);
  m.init(key);
  m.update(password.getBytes());
  byte[] mac = m.doFinal();
  return new String(Base64.encode(mac, Base64.DEFAULT));
}
