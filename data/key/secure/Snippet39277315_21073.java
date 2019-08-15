byte[] data = getDataBytes();
Mac mac = Mac.getInstance("HMAC-SHA256");
mac.init(new SecretKeySpec(key, "HMAC-SHA256"));
byte[] encryptedBytes = mac.doFinal(data);
