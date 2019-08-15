        try {
        String arg = "b74420f5a4d9abfd2072c9d936dd53e2de2aa790822ad1608807bda3e176b335c51902ca2177824198181ce8bea85de132aaea1104fd043e4ad2c0af705bda966b5d2f92a6ab5170d161eb1e8f7a6b1d5fba673f8a4dcebe55407ef9707782c91b17527af820a2c3a3b586341ae54ef03739074d4738e3ff35257bdfb9233c53";

        byte[] bytes = org.apache.commons.codec.binary.Hex.decodeHex(arg.toCharArray());
        System.out.println(new String(bytes, "UTF-8"));

        String message = "oussaki";

        byte[] publicBytes = org.apache.commons.codec.binary.Hex.decodeHex(arg.toCharArray());

        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
        try {

            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey pubKey = keyFactory.generatePublic(keySpec);
            // Encrypt the message
            byte[] encryptedBytes = null;
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);

            encryptedBytes = cipher.doFinal(message.getBytes());
            System.out.println(encryptedBytes);

            byte[] b2 = new byte[encryptedBytes.length + 1];
            b2[0] = 1;
            System.arraycopy(encryptedBytes, 0, b2, 1, encryptedBytes.length);
            String s = new BigInteger(b2).toString(36);
            System.out.println("Encrypted text" + s);

        } catch (BadPaddingException | IllegalBlockSizeException | InvalidKeyException
                | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException k) {
            k.printStackTrace();
        }
    } catch (DecoderException e) {
        e.printStackTrace();
    }
