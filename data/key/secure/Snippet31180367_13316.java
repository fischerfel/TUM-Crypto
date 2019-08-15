private String generateSasToken(String uri, String keyName, String key){
    String ret = "";

    long tokenExpirationTime = (System.currentTimeMillis() / 1000) + (10 * 365 * 24 * 60 * 60);

    try {
        String stringToSign = new URL(uri).toString() + "\n" + tokenExpirationTime;
        SecretKey secretKey = null;

        byte[] keyBytes = key.getBytes("UTF-8");

        Mac mac = Mac.getInstance("HMACSHA256");

        secretKey = new SecretKeySpec(keyBytes, mac.getAlgorithm());

        mac.init(secretKey);

        String signature = Base64.encodeToString(mac.doFinal(stringToSign.getBytes("UTF-8")), Base64.DEFAULT);
        ret = String.format("SharedAccessSignature sr=%s&sig=%s&se=%s&skn=%s",
                URLEncoder.encode(uri),
                URLEncoder.encode(signature),
                String.valueOf(tokenExpirationTime),
                keyName);
    } catch (MalformedURLException e) {
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        e.printStackTrace();
    } catch (InvalidKeyException e) {
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    }

    return ret;
}
