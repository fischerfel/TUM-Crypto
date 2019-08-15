String stored[] = index.split("\\$");
KeySpec spec1 = new PBEKeySpec("haslo1234fdfgds".toCharArray(), stored[0].getBytes(), 65536, 128);
SecretKeyFactory f1 = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512");
byte[] hash1 = f.generateSecret(spec1).getEncoded();
if(stored[1].equals(enc.encodeToString(hash1))) return true;
