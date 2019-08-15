public String createRequestSignature(String signatureBase, String consumerSecret, String tokenSecret) 
        throws Exception {
        try {
            String keyString    = URIUtil.encode(consumerSecret) + '&' + URIUtil.encode(tokenSecret);
            Debug.i("key " + keyString);
            byte[] keyBytes     = keyString.getBytes("UTF-8");
            SecretKey key       = new SecretKeySpec(keyBytes, MAC_NAME);
            Mac mac             = Mac.getInstance(MAC_NAME);
            mac.init(key);
            byte[] text         = signatureBase.getBytes("UTF-8");
            return Base64.encodeBytes(mac.doFinal(text)).trim();
        } catch (GeneralSecurityException e) {
            throw e;
        } catch (UnsupportedEncodingException e) {
            throw e;
        }
    }
