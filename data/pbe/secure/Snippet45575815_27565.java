PBEKeySpec spec = new PBEKeySpec(rawPass.toCharArray(), mSalt.getBytes(), iterations, 256);
