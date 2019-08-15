SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
PBEKeySpec pbeKeySpec = new PBEKeySpec(charData,salt,iterations,bitLength);
hash = factory.generateSecret(pbeKeySpec).getEncoded();
