SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC", provider);
PBEKeySpec pbeKeySpec = new PBEKeySpec("Password12".toCharArray());
SecretKey key = factory.generateSecret(pbeKeySpec);
