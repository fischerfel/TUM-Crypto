    byte[] pt = new byte[16];
    SecretKeyFactory fact = SecretKeyFactory.getInstance("DESede");
    Cipher desEDE = Cipher.getInstance("DESede/ECB/NoPadding");

    {
        // usual 2-key triple DES:
        byte[] keyData = Hex.decode("112233445566778811223344556677881122334455667788");
        SecretKey generatedSecret = fact.generateSecret(new SecretKeySpec(keyData, "DESede"));
        desEDE.init(Cipher.ENCRYPT_MODE, generatedSecret);
        byte[] ct = desEDE.doFinal(pt);
        System.out.println(Hex.toHexString(ct)); // result: 6FB23EAD0534752B 
    }

    {
        // "zero padded" 2-key triple DES:
        byte[] keyData = Hex.decode("112233445566778811223344556677880000000000000000");
        SecretKey generatedSecret = fact.generateSecret(new SecretKeySpec(keyData, "DESede"));
        desEDE.init(Cipher.ENCRYPT_MODE, generatedSecret);
        byte[] ct = desEDE.doFinal(pt);
        System.out.println(Hex.toHexString(ct)); // result: 8ca64de9c1b123a7 
    }
