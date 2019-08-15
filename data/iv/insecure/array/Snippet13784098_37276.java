byte[] iv = new byte[] { 0x0, 0x1, 0x2, 0x3, 0x4, 0x5, 0x6, 0x7, 0x8, 0x9, 0xA, 0xB, 0xC, 0xD, 0xE, 0xF }; 
IvParameterSpec ivSpec = new IvParameterSpec(iv);

SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
Cipher cipher = Cipher.getInstance("AES/CFB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, skeySpec, ivSpec);
