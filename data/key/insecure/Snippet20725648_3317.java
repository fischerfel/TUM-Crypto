public static void main(String[] args) {
  String str1 = "a4d1b77bbb1a4a5ca695ad72c84b77e5";
  byte[] keyBytes = str1.getBytes();
  SecretKeySpec localMac = new SecretKeySpec(
      keyBytes, "HmacSHA256");
  final String inputStr = "{\"_uid\":\"3396112\",\"_csrftoken\":"
      + "\"a23482932482sdsf4428\","
      + "\"media_id\":\"616150302791211280_187036957\"}";
  try {
    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(localMac);
    // Compute the hmac on input data bytes
    byte[] arrayOfByte = mac.doFinal(inputStr
        .getBytes());
    BigInteger localBigInteger = new BigInteger(1,
        arrayOfByte);
    String str3 = String.format("%0"
        + (arrayOfByte.length << 1) + "x",
        new Object[] { localBigInteger });
    System.out.println(str3);
  } catch (InvalidKeyException e) {
    e.printStackTrace();
  } catch (NoSuchAlgorithmException e) {
    e.printStackTrace();
  } catch (IllegalStateException e) {
    e.printStackTrace();
  }
}
