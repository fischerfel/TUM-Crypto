final char[] cPassword = password.toCharArray();
final byte[] bSalt = Base64.getDecoder().decode(utente.getSalt().getBytes());
KeySpec spec = new PBEKeySpec(cPassword, bSalt, 10000, 64 * 8);
SecretKeyFactory key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
byte[] hashedPassword = key.generateSecret(spec).getEncoded();
String hashPassword = new String(Base64.getEncoder().encode(hashedPassword));
