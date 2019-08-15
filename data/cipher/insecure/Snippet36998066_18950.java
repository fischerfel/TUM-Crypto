SecretKeySpec secretkeyspec = new SecretKeySpec("password".getBytes(), "AES");
Cipher cipher = Cipher.getInstance("AES");
cipher.init(Cipher.ENCRYPT_MODE, secretkeyspec);
byte[] encoded = cipher.doFinal(s.getBytes());
System.out.println(Arrays.toString(encoded));