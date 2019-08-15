getSecurityKeys(apikey, pzsecret,str);

private static Map<String, String> getSecurityKeys(String appId,
   String secureId, String payLoad) throws Exception {
  Map<String, String> returnMap = new HashMap<String, String>();
  try {
   returnMap.put(NONCE, Long.toString(nonce));
   returnMap.put(APIKEY, appId);
   returnMap.put(TIMESTAMP, Long.toString(System.currentTimeMillis()));
   returnMap.put(TOKEN, MerchantToken);
   returnMap.put(APISECRET, pzsecret);
   returnMap.put(PAYLOAD, payLoad);
   returnMap.put(AUTHORIZE, getMacValue(returnMap));
   authorizationHeader = returnMap.get(AUTHORIZE);
   return returnMap;

  } catch (NoSuchAlgorithmException e) {
   throw new RuntimeException(e.getMessage(), e);
  }
 }

 public static String getMacValue(Map<String, String> data) throws Exception {
  Mac mac = Mac.getInstance("HmacSHA256");
  String apiSecret = data.get(APISECRET);
  SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(),
    "HmacSHA256");
  mac.init(secret_key);
  StringBuilder buff = new StringBuilder();
  buff.append(data.get(APIKEY)).append(data.get(NONCE))
    .append(data.get(TIMESTAMP));
  if (data.get(TOKEN) != null)
   buff.append(data.get(TOKEN));
  if (data.get(PAYLOAD) != null)
   buff.append(data.get(PAYLOAD));
  byte[] macHash = mac.doFinal(buff.toString().getBytes("UTF-8"));
  String authorizeString = Base64.encodeBase64String(toHex(macHash));
  return authorizeString;
 }