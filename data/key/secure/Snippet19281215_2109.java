    private void setupMac() throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException
    {

        byte[] secretyKeyBytes = KEY_SECRET.getBytes("UTF-8");
        signingKey = new SecretKeySpec(secretyKeyBytes, "HmacSHA256");
        mac = Mac.getInstance("HmacSHA256");
        mac.init(signingKey);
    }

    private String hmac(String stringToSign) {
        String signature = null;
        byte[] data;
        byte[] rawHmac;
        try {
            data = stringToSign.getBytes("UTF-8");
            rawHmac = mac.doFinal(data);
            signature = new String(Base64.encode(rawHmac, Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("UTF-8" + " is unsupported!", e);
        }
        return signature;
    }

    private String percentEncodeRfc3986(String s) {
        String out;
        try {
            out = URLEncoder.encode(s, "UTF-8").replace("+", "%20")
                    .replace("*", "%2A").replace("%7E", "~");
        } catch (UnsupportedEncodingException e) {
            out = s;
        }
        return out;
    }
