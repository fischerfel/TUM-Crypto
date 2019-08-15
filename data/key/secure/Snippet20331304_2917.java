// Base 64 decode
byte[] keyBytes = Base64.decodeBase64(encodedKey.getBytes("UTF-8"));

// Need to put the same key generation algorithm in here:
SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
