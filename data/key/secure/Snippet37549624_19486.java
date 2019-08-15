private String sign(String data) throws Exception {

    InputStream input = getClass().getResourceAsStream(
            securityWebApi.getSecurityFilterConfiguration().getAzureSignatureKey());

    Reader reader = new InputStreamReader(input, "UTF-8");

    byte[] key = IOUtils.toByteArray(reader, Charset.forName("UTF-8"));

    LOG.debug("got key array : " + new String(key));

    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
    sha256_HMAC.init(secret_key);

    String testSign = Hex
            .encodeHexString(sha256_HMAC.doFinal("http%3a%2f%2fschemas.xmlsoap.org".getBytes("UTF-8")));

    LOG.debug("test sign == " + testSign);

    String testSign2 = "Bla"
                +"bla";

    LOG.debug("test sign2== " + Hex
            .encodeHexString(sha256_HMAC.doFinal(testSign2.getBytes("UTF-8"))));

    return Hex.encodeHexString(sha256_HMAC.doFinal(data.getBytes("UTF-8")));
}
