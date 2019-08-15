char[] passwordChars = password.toCharArray();
byte[] saltBytes = Constantes.SALT.getBytes();
PBEKeySpec spec = new PBEKeySpec(passwordChars, saltBytes, Constantes.ITERATIONS,192);

try {
    SecretKeyFactory  key = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
    byte[] hashedPassword = key.generateSecret(spec).getEncoded();   
    return String.format("%x", new BigInteger(hashedPassword));
} catch {[..]}
