 PBEKeySpec keySpec = new PBEKeySpec(password);

 SecretKeyFactory keyFactory = SecretKeyFactory
        .getInstance("PBEWITHMD5ANDTRIPLEDES"/* "PBEWithSHAAndTwofish-CBC" */);

 SecretKey key = keyFactory.generateSecret(keySpec);

 PBEParameterSpec paramSpec = new PBEParameterSpec(salt,
                    MD5_ITERATIONS);

Cipher cipher = Cipher.getInstance("PBEWITHMD5ANDTRIPLEDES");
            cipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);//here Ex.

byte[] ciphertext = cipher.doFinal(plaintext); 
