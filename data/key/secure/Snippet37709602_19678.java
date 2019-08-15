SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(),HMAC_SHA1_ALGORITHM);

Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM); mac.init(signingKey);

byte[] bytes = mac.doFinal(signatureString.getBytes()); return BASE64.encodeBAse64String(bytes);
