    // Decode the secret key
    byte[] decodedSecret;
    try
    {
        decodedSecret = Base64.decode(SECRET_KEY);
    }
    catch (Base64DecodingException ex)
    {
        System.out.println("Failed to decode secret key.");
        return null;
    }

    // Make the header parameters
    long timestamp = (new GregorianCalendar()).getTimeInMillis() / 1000;
    String preSign = "" + timestamp + "GET" + BASE_URL + "/accounts";

    byte[] encodedhash;
    try
    {
        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        try
        {
            sha256_HMAC.init(new SecretKeySpec(decodedSecret, "HmacSHA256"));
        }
        catch (InvalidKeyException ex)
        {
            System.out.println("Failed due to invalid key exception.");
            System.out.println(ex.getMessage());
            return null;
        }
        encodedhash = sha256_HMAC.doFinal(preSign.getBytes());
    }
    catch (NoSuchAlgorithmException ex)
    {
        System.out.println("Failed to make SHA-256 encode because of no such algorithm.");
        return null;
    }

    HashMap<String, String> parameters = new HashMap<>();
    parameters.put("CB-ACCESS-KEY", API_KEY);
    parameters.put("CB-ACCESS-SIGN", Base64.encode(encodedhash));
    parameters.put("CB-ACCESS-TIMESTAMP", "" + timestamp);
    parameters.put("CB-ACCESS-PASSPHRASE", PASSPHRASE);

    // Send the request
    String response = sendGet(BASE_URL + "/accounts", parameters);
