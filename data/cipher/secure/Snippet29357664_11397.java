Cipher rsa = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
rsa.init(Cipher.WRAP_MODE, keyPair.getPublic());
byte[] wrapped = rsa.wrap(aesKey);

rsa.init(Cipher.UNWRAP_MODE, keyPair.getPrivate());
SecretKey unwrappedAESKey = (SecretKey) rsa.unwrap(wrapped, "RSA", Cipher.SECRET_KEY);
