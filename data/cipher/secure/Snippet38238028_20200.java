if(jsonObject.getBoolean("success")){
        String timeStamp = jsonObject.getString("timestamp");
        String publickey_mod = jsonObject.getString("publickey_mod");
        String keyexp = jsonObject.getString("publickey_exp");
        String modulus_preHex = new BigInteger(1, publickey_mod.getBytes("UTF-8")).toString(16);
        String exponent_preHex = new BigInteger(1, keyexp.getBytes("UTF-8")).toString(16);
        BigInteger modulus_post = new BigInteger(modulus_preHex);
        BigInteger exponent_post = new BigInteger(exponent_preHex);
        PublicKey key = getEncrpytedKey(modulus_post, exponent_post);

        Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA1AndMGF1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] passArray = pass.getBytes("UTF-8");
        String encode = Base64.encodeBase64(cipher.doFinal(passArray)).toString();
        System.out.println(encode + " - encode");


    }
