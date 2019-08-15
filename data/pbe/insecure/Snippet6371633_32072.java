SecretKeyFactory skFactory = SecretKeyFactory.getInstance("PBEWithSHA1AndDESede");
SecretKey key = skFactory.generateSecret(new PBEKeySpec("<some password>".toCharArray()));
