String key;
byte[] keydata = hashFunctionToMakeToKeytheRightSize(key);
SecretKeySpec secretKeySpec = new SecretKeySpec(keydata, "AES");
