            KeyFactory factory = KeyFactory.getInstance("RSA");
            RSAPrivateKeySpec spec;
            spec = new RSAPrivateKeySpec(modulus,privateKey);
            RSAPrivateKey priPEM = (RSAPrivateKey) factory.generatePrivate(spec);
            Cipher enc = Cipher.getInstance("RSA");
            enc.init(Cipher.WRAP_MODE, priPEM);
            byte[] encryptedKey = enc.wrap(priPEM);
