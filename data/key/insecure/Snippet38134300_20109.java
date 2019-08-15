SecretKey sk = new SecretKeySpec(new byte[16], "AES");
Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
GCMParameterSpec gcmSpec = new GCMParameterSpec(128, new byte[12]);
cipher.init(Cipher.WRAP_MODE, sk, gcmSpec);
byte[] wrappedKey = cipher.wrap(sk);
System.out.println(Hex.toHexString(wrappedKey));

cipher.init(Cipher.UNWRAP_MODE, sk, gcmSpec);
SecretKey unwrap = (SecretKey) cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);
System.out.println(Hex.toHexString(unwrap.getEncoded()));
