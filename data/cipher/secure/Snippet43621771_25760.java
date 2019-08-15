                PBEKeySpec keySpec = new PBEKeySpec(passwordServer.toCharArray());
            SecretKeyFactory kf;

                kf = SecretKeyFactory.getInstance("PBEWithHmacSHA256AndAES_128");

                secretKey = kf.generateSecret(keySpec); 
                byte[] ivBytes = {0x11,0x12,0x13,0x14,0x15,0x16,0x17,0x18,
                        0x19,0x1A,0x1B,0x1C,0x1D,0x1E,0x1F,0x20};
                IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);

                PBEParameterSpec spec = new PBEParameterSpec(salt, 20, ivSpec);

                c = Cipher.getInstance("PBEWithHmacSHA256AndAES_128");
                c.init(Cipher.ENCRYPT_MODE, secretKey, spec);
                params = c.getParameters();
