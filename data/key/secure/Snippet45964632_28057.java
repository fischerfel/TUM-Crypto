    long nonce = (new Date()).getTime();
    JSONObject json = new JSONObject();
    json.put("currency", "BTC");
    json.put("price", 3000000);
    json.put("qty", 0.01);
    json.put("access_token", publicKey);
    json.put("nonce", nonce);

    System.out.println(json.toString());

    Client client = ClientBuilder.newClient();
    Entity<String> payload = Entity.json(json.toString());
    String message = json.toString(); 
    Response response = client.target(baseAddress+apiAddress)
      .request(MediaType.APPLICATION_JSON_TYPE)
      .header("content-type", "application/json")
      .header("accept", "application/json")
      .header("X-COINONE-PAYLOAD", payload)
      .header("X-COINONE-SIGNATURE", CreateToken(message, privateKey.toUpperCase()))
      .post(payload);

    String body = response.readEntity(String.class);
    System.out.println(body);

    ///////////
    private static String CreateToken(String message, String secretKey)
{
    String hash = "";
    try {    

        Mac sha512_HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec secret_key = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
        sha512_HMAC.init(secret_key);

        hash = Base64.encodeBase64String(sha512_HMAC.doFinal(message.getBytes()));

    }
    catch (Exception e){
        System.out.println("Error");
    }
    System.out.println(hash);
    return hash;
}
