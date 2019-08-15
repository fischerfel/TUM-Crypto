if (request.getParameter("signed_request") != null)
{
    signed_request  =   request.getParameter("signed_request");
    //out.print("<br>signed_request: "+signed_request);
    try
    {
        System.out.println("signed_request"+signed_request);
        System.out.println("signed_request1");
        fb_data = parse_signed_request(signed_request, secretKey);
    }
    catch(Exception e)
    {
        System.out.println("error"+e);
    }
}





  public static Map parse_signed_request(String input, String secret) throws Exception {
    return parse_signed_request(input, secret, 3600);
  }

  public static Map parse_signed_request(String input, String secret, int max_age) throws Exception {
    String[] split = input.split("[.]", 2);

    String encoded_sig = split[0];
    String encoded_envelope = split[1];
    JSONParser parser = new JSONParser();
    Map envelope = (Map) parser.parse(new String(base32_url_decode(encoded_envelope)));

    String algorithm = (String) envelope.get("algorithm");

    if (!algorithm.equals("HMAC-SHA256")) {
      throw new Exception("Invalid request. (Unsupported algorithm.)");
    }

    if (((Long) envelope.get("issued_at")) < System.currentTimeMillis() / 1000 - max_age) {
      throw new Exception("Invalid request. (Too old.)");
    }

    byte[] key = secret.getBytes();
    SecretKey hmacKey = new SecretKeySpec(key, "HMACSHA256");
    Mac mac = Mac.getInstance("HMACSHA256");
    mac.init(hmacKey);
    byte[] digest = mac.doFinal(encoded_envelope.getBytes());

    if (!Arrays.equals(base32_url_decode(encoded_sig), digest)) {
      throw new Exception("Invalid request. (Invalid signature.)");
    }

    return envelope;
  }
