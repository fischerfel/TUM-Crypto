        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBEWithMD5AndTripleDES");
        SecretKey key = factory.generateSecret(keySpec);

        // instead of key.getAlgorithm(). For compatibility with JDK 1.5
        String newAlgorithm = "PBEWithMD5AndDES"; // the same value as in JDK 1.5

        ecipher = Cipher.getInstance(newAlgorithm);
