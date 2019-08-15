        byte[] hmacKey = BaseEncoding.base16().decode(HMAC_KEY);
        SecretKeySpec signingKey = new SecretKeySpec(hmacKey, HMAC_SHA256_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA256_ALGORITHM);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes(C_UTF8));



        return BaseEncoding.base64().encode(rawHmac);
