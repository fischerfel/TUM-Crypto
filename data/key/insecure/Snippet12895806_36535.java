String hmac_key = "my hmac key";
String encrypted_message_b64 = "fCyVmpFAZxv9Utui2QWGUtoGJ//Zr5aH+1PV31ry/dwX3yVdeEMIMW/dfoA9ihbnYrnoSnb2yyfO\nrBYoy0JlDvWz8GJ6dY643lDTj7xcw8Q=";
final Mac hmac = Mac.getInstance("HmacSHA256");
hmac.init(new SecretKeySpec(hmac_key.getBytes("UTF-8"), "HmacSHA256"));
byte[] signature = hmac.doFinal(encrypted_message_b64.getBytes("UTF-8"));
System.out.println(Hex.encodeHexString(signature));

d5bc0b58f43c6f6611f63822d22f99e18c51a33251a5a1c0c7712b4c7fb1ad24
