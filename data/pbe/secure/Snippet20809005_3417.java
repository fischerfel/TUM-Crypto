PBKDF_SecretKeyFactory kf = new PBKDF_SecretKeyFactory("HmacSHA512");
KeySpec ks = new PBEKeySpec(password,salt,iterations,bitlen);
byte key[] = kf.engineGenerateSecret(ks).getEncoded();
