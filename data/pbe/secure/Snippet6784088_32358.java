public String AESEncrypt(String sKey, String PlainMsg)
                throws Exception {
            //Try use some Android based alert dialog to catch this exception.
            if (sKey == null) {
                Log.e("SecureChat", "IllegalArgumentException Catched");
                throw new IllegalArgumentException ("NULL Secret NOT ALLOWED!");
            }           
            /*Old Method
            //byte[] rawKey = getRawKey(sKey.getBytes("UTF-8"));
            byte[] rawKey = getRawKey(sKey.getBytes());
            //Encrypt start
            SecretKeySpec keySpec = new SecretKeySpec(rawKey, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            //byte[] cipherText = cipher.doFinal(PlainMsg.getBytes("UTF-8"));
            byte[] cipherText = cipher.doFinal(PlainMsg.getBytes());
            return Base64Encoded(cipherText);
            */
            //New Method
            byte[] salt = getSalt();
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWITHSHA256AND256BITAES-CBC-BC");
            KeySpec spec = new PBEKeySpec(sKey.toCharArray(), salt, 1024, 256); 
            SecretKey tmp = factory.generateSecret(spec);
            SecretKey secret = new SecretKeySpec(tmp.getEncoded(), "AES");
            //Encryption Process
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secret);
            byte[] cipherText = cipher.doFinal(PlainMsg.getBytes());
            //return Base64Encoded(cipherText);
            //Hex
            return toHex(cipherText);
        }
