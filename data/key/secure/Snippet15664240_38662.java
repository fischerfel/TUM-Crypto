                SecretKeySpec skey = new SecretKeySpec(key, "AES");
                Cipher cipher = Cipher.getInstance(AES/ECB/NoPadding, "BC");
                cipher.init(Cipher.DECRYPT_MODE, skey);
                result = decrypt(cipher, input);
