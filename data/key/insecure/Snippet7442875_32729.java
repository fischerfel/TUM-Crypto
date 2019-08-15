String secretAccessKey = "secret1234678901";        
SecretKeySpec keySpec = new SecretKeySpec(secretAccessKey.getBytes(UTF-8), "HmacSHA256");
Mac mac = Mac.getInstance(this.MAC_ALGO);
mac.init(keySpec); // here it breaks
byte[] encoded = mac.doFinal(
    request.toString().getBytes(this.CHARSET));
return Base64.encodeBase64URLSafeString(encoded);
