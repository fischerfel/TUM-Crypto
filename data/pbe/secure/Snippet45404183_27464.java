byte[] salt = parts[1].getBytes("utf-8");
byte[] hash = fromHex(parts[2]);
PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(), salt, 1000, 20*8);
