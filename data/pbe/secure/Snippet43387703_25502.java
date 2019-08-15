PBEKeySpec keySpec = new PBEKeySpec(password.toCharArray(), Hex.decode(salt), 1024, 256);
