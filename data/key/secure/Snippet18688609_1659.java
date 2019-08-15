BASE64Decoder decoder = new BASE64Decoder();
String[] parts = request.getParameter("signed_request").split("\\.", 2);
String signature = new String(decoder.decodeBuffer(parts[0]), "UTF-8");
String rawData = new String(decoder.decodeBuffer(parts[1]), "UTF-8");

if (!isSignedRequestValid(request, obj, signature, parts[1]))
...

    private boolean isSignedRequestValid(HttpServletRequest request, JSONObject obj, String signature, String data) throws IOException
    {

            String expectedSignature = generateSha256Signature(data, FacebookAppSecretKey);

            if (!signature.equals(expectedSignature))
            {
                log("Facebook signatures do not match, expected: " + expectedSignature + ", received: " + signature);
                return false;
            }
         }


    private String generateSha256Signature(String data, String key) throws Exception
    {
      java.net.URLDecoder decoder = new java.net.URLDecoder();
      data = decoder.decode(data, "UTF-8");  // mostly here for testing, doesn't seem to make a difference
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(secretKey);
        byte[] hmacData = mac.doFinal(data.getBytes("UTF-8"));
        return new String(hmacData);
    }
