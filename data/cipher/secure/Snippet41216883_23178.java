//ENCRYPT
SecretKey secretKey = (SecretKey) pkr.getValue("AES");
RSAPublicKey = (RSAPublicKey) pkr.getValue("EPK");

rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding",        "SunJCE");
rsaCipher.init(Cipher.WRAP_MODE, RSAPublicKey);
wrappedKey = rsaCipher.wrap(secretKey);
fos.write(wrappedKey);




//DECRYPT
rsaCipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-256AndMGF1Padding",   "SunJCE");
rsaCipher.init(Cipher.UNWRAP_MODE, RSAPrivateKey);

fin.read(wrappedSecretKey);
SecretKey secretKey = (SecretKey) rsaCipher.unwrap(wrappedSecretKey, "AES",   Cipher.SECRET_KEY);
