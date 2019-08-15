char[] pass = ... // your password
byte[] codeBytes = ... // up to 255 bytes you want to protect

// generate wrapping key from password
SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
SecureRandom rand = SecureRandom.getInstance("SHA1PRNG");
byte[] salt = new byte[16]; rand.nextBytes(salt);
SecretKey kek = f.generateSecret(new PBEKeySpec(pass, salt, 1000, 128));
kek = new SecretKeySpec(password.getEncoded(), "AES"); // convert into AES

// RFC3537 padding (lengthbyte)
byte[] wrappedCodeBytes = new byte[codeBytes + 1 % 8];
System.arraycopy(codeBytes,0,wrappedCodeBytes,1,wrappedCodeBytes.length);
paddedCodeBytes[0]=(byte)codeBytes.length;
byte[] pad = new byte[paddedCodeBytes.length - codeBytes.length -1]; rand.nextBytes(pad);
System.arraycopy(pad,0,paddedCodeBytes,codeBytes.length+1,pad.length);
// AESWrap is WRAP_MODE:needs a SecretKey 
SecretKey paddedCodeKey = new SecretKeySpec(paddedCodeBytes, "RAW");

// now wrap the password with AESWrap kek is 128 bit
Cipher c = Cipher.getInstance("AESWrap"); // default IV
c.init(Cipher.WRAP_MODE, kek);
byte[] result = c.warp(paddedCodeKey);
