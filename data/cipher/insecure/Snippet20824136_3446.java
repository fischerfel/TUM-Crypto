        secret_key = KeyGenerator.getInstance("DES").generateKey();
        alogrithm_specs = new IvParameterSpec(initialization_vector);
        // set encryption mode ...
        encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
        encrypt.init(Cipher.ENCRYPT_MODE, secret_key, alogrithm_specs);
        //out.print("actual secret_key:"+secret_key);

        String keyString = encoder.encode(secret_key.getEncoded());
        //out.print("keyString:"+keyString);

        byte[] encodedKey = decoder.decodeBuffer(keyString);
        //out.print("byte[]:"+encodedKey);

        secret_key= new SecretKeySpec(encodedKey,0,encodedKey.length, "DES");
        //out.print("after encode & decode secret_key:"+secret_key);
