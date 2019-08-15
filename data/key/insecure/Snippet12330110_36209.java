@Test
public void encodeTest() {

    String HMAC_SHA1_ALGORITHM = "HmacSHA1";
    String EXPECTED_BASE_64 = "g9OrJ8pQNYprnXuBPFXcirrqpxE=";

    String text = "encodeme";
    String result;

    try {
        SecretKeySpec signingKey = new SecretKeySpec(
                "MSbN2crsrdTEsLetTixpV46q+fTZotdZjwoEpO62vYk=".getBytes(),
                HMAC_SHA1_ALGORITHM);
        // Get an hmac_sha1 Mac instance and initialise with the signing key
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        // Compute the hmac
        byte[] rawHmac = mac.doFinal(text.getBytes());
        // Convert raw bytes to Hex
        byte[] hexBytes = new Hex().encode(rawHmac);
        // Covert array of Hex bytes to a String

        result = new String(hexBytes, "ISO-8859-1");
        // Ok, this matches with the web
        System.out.println("HEX:" + result);

        String encodedBase64 = new String(Base64.encodeBase64(hexBytes));
        System.out.println("BASE64:" + encodedBase64);

        // In the web i get a smaller chain, why?
        System.out.println("EXPECTED BASE64:" + EXPECTED_BASE_64);
        Assert.assertEquals(EXPECTED_BASE_64, encodedBase64);

    } catch (InvalidKeyException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (IllegalStateException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    } catch (UnsupportedEncodingException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }

}
