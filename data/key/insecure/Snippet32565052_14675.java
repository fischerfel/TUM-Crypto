        private static final String HMAC_ALGORITHM = "HmacSHA256";
        String key = "hush";
        String data = "shop=some-shop.myshopify.com&timestamp=1337178173";    
        SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(),HMAC_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        mac.init(keySpec);
        byte[] rawHmac = mac.doFinal(data.getBytes());
        System.out.println(Hex.encodeHexString(rawHmac));
