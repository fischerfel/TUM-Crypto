        byte[] keyBytes = GenericConstants.SECRET_KEY.getBytes();
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, "HmacSHA256");
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(json.getBytes());
        byte[] hexBytes = new Hex().encode(rawHmac);
        String signetJson =  String(hexBytes, "UTF-8");
