Mac mac = null;
mac = Mac.getInstance("HMAC-SHA1");
String secret = oauthConsumerSecretKeyStringValue+"&";
        mac.init(new SecretKeySpec(secret.getBytes("utf-8"), "HMAC-SHA1"));
signature = Base64.encodeToString(mac.doFinal(signatureBaseString.getBytes("utf-8")), 0).trim();
