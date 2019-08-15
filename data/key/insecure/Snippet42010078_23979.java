  Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        SecretKeySpec key = new SecretKeySpec("testPass".getBytes(), "Blowfish");
Cipher cipher = Cipher.getInstance("Blowfish/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
