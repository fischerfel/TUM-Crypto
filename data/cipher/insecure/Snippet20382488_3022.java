Cipher c = Cipher.getInstance("ARC4");
c.init(Cipher.DECRYPT_MODE, new SecretKeySpec("BrianIsInTheKitchen".getBytes(), "ARC4"));
