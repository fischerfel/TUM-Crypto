    Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
    SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
    sha256_HMAC.init(secretKey);

    byte[] hash = sha256_HMAC.doFinal(policy.getBytes());
    byte[] hexB = new Hex().encode(hash);
    String check = Hex.encodeHexString(hash);
    String sha256 = DigestUtils.sha256Hex(secret.getBytes());
