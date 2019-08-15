    final String HMAC_SHA1_ALGORITHM = "HmacSha256";
    final String secret = "D6FECF42-2D1E-4db9-A273-EB34130EA137";
    final SecretKeySpec signingKey = new SecretKeySpec(secret.toLowerCase().getBytes(),HMAC_SHA1_ALGORITHM);
    final Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
    mac.init(signingKey);
    mac.update("0.4726005982213448".getBytes());
    mac.update("test@test.com".getBytes());
    final byte[] rawHmac = mac.doFinal();
    for (final byte element : rawHmac)
    {
       result += Integer.toString((element & 0xff) + 0x100, 16).substring(1);
    }
    System.out.println( result);
