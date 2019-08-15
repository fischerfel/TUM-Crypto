Key key = SecretKeyFactory.getInstance(algorithm).generateSecret(new PBEKeySpec(password.toCharArray()));
