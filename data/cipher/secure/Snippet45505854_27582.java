String key = client.getPublicKey(nonce).getRSAEncryptionKey().toString();
key = key.replace("-----BEGIN RSA PUBLIC KEY-----\n", "");
key = key.replace("-----END RSA PUBLIC KEY-----", "");
byte[] keyBytes = Base64.getDecoder().decode(key); //bytes of key
Cipher cipher_RSA;
        try {
            cipher_RSA = Cipher.getInstance("RSA");
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pk = keyFactory.generatePublic( spec);

            cipher_RSA.init(Cipher.ENCRYPT_MODE, pk); 
            return cipher_RSA.doFinal(message);
        }catch(Exception e){}
