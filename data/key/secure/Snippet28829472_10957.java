byte[] encodedKey     = Base64.decode(stringKey, Base64.DEFAULT);
SecretKey originalKey = new SecretKeySpec(encodedKey, 0, encodedKey.length, "AES");
