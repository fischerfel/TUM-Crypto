Key rootKey = new SecretKeySpec(keyBytes, "AES");
KeyGenerator generator = KeyGenerator.getInstance("AES");
generator.init(128);
Key keyToWrap = generator.generateKey();

Cipher cipher = Cipher.getInstance("AESWRAP");
cipher.init(Cipher.WRAP_MODE, rootKey);
byte[] wrappedKey = cipher.wrap(keyToWrap);

Cipher uncipher = Cipher.getInstance("AESWRAP");
uncipher.init(Cipher.UNWRAP_MODE, rootKey);
Key unwrappedKey = uncipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);
