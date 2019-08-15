// DECODE YOUR BASE64 STRING
// REBUILD KEY USING SecretKeySpec

    byte[] encodedKey     = Base64.decode(stringKey, Base64.DEFAULT);
    SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
