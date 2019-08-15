SecretKeySpec eks = new SecretKeySpec(k, "AES");
Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
c.init(Cipher.ENCRYPT_MODE, eks, new GCMParameterSpec(128, iv));
