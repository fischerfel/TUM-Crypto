KeySpec spec = new PBEKeySpec(passsword.toCharArray(), salt, 
   iterations, derivedKeyLength);
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

return f.generateSecret(spec).getEncoded();
