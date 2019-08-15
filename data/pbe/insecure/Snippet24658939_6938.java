SecretKey secretKey = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(new PBEKeySpec("a_private_key".toCharArray()));
Cipher cipher = Cipher.getInstance("PBEWithMD5AndDES")
cipher.init(Cipher.ENCRYPT_MODE, secretKey, 
            new PBEParameterSpec(
               new byte[] { (byte) 0x30, (byte) 0x31, (byte) 0x32, (byte) 0x33,
                            (byte) 0x34, (byte) 0x35, (byte) 0x36, (byte) 0x37, },
               15);
byte[] crypted = cipher.doFinal("StringToEncrypt".getBytes(UTF8));
