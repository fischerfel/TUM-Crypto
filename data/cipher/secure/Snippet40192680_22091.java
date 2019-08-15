   try {
        String arg = "b74420f5a4d9abfd2072c9d936dd53e2de2aa790822ad1608807bda3e176b335c51902ca2177824198181ce8bea85de132aaea1104fd043e4ad2c0af705bda966b5d2f92a6ab5170d161eb1e8f7a6b1d5fba673f8a4dcebe55407ef9707782c91b17527af820a2c3a3b586341ae54ef03739074d4738e3ff35257bdfb9233c53";
        String message = "plain text";
        byte[] publicBytes = org.apache.commons.codec.binary.Hex.decodeHex(arg.toCharArray());

        BigInteger modulus = new BigInteger(1, publicBytes);
        BigInteger publicExponent = BigInteger.valueOf(65537L);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(modulus, publicExponent);

        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pubKey = keyFactory.generatePublic(keySpec);

        try {
            // decrypts the message
            byte[] encryptedBytes = null;
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            encryptedBytes = cipher.doFinal(message.getBytes());

            System.out.println( "Encrypted text : "+ convertToHexString(encryptedBytes));
        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException
                | NoSuchPaddingException | NoSuchAlgorithmException k) {
            k.printStackTrace();
        }
    } catch (DecoderException e) {
        e.printStackTrace();
    }
