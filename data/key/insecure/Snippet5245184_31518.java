String Key = "Something";
byte[] KeyData = Key.getBytes();
SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
Cipher cipher = Cipher.getInstance("Blowfish");
cipher.init(Cipher.ENCRYPT_MODE, KS);
