private boolean verifySignature(String payload, String signature) throws Exception {

        if (!signature.startsWith("sha1=")) {

            return false;

        }

        String expected = signature.substring(5);       

        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);

        SecretKeySpec signingKey = new SecretKeySpec(applicationSecret.getBytes(), HMAC_SHA1_ALGORITHM);

        mac.init(signingKey);

        byte[] rawHmac = mac.doFinal(payload.getBytes());

        String actual = new String(Hex.encode(rawHmac));

        return expected.equals(actual);

    }
