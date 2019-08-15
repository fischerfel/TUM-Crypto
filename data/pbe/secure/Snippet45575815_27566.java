PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), mSalt.getBytes(), iterations, 256);
