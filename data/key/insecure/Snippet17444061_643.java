String stringToSign = "GET" + "\n" +
                    "webservices.amazon.com" + "\n" +
                    "/onca/xml" + "\n" +
                    "AWSAccessKeyId=AKIAIOSFODNN7EXAMPLE&ItemId=0679722769&Operation=ItemLookup&ResponeGroup=ItemAttributes%2COffers%2CImages%2CReviews&Service=AWSECommerceService&Timestamp=2009-01-01T12%3A00%3A00Z&Version=2009-01-06";


    SecretKeySpec keySpec = new SecretKeySpec(
            "1234567890".getBytes(),
            "HmacSHA256");

    Mac mac = Mac.getInstance("HmacSHA256");
    mac.init(keySpec);


    byte[] result = mac.doFinal(stringToSign.getBytes());
    String encodedResult = Base64.encodeBase64String(result);
    System.out.println("encodedResult: "+encodedResult);

    String urlEncodedResult = URLEncoder.encode(encodedResult, "UTF-8").replace("+", "%2B").replace("*", "%2A").replace("%7E", "~");
    System.out.println("ulrEncodedResult: "+urlEncodedResult);
