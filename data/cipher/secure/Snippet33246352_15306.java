public static String encrypt(String text, Context c, String pub) {
        try {
            byte[] pubKey = Base64.decode(pub, 0);

            KeyFactory factory = KeyFactory.getInstance("RSA");
            EncodedKeySpec keySpec = new X509EncodedKeySpec(pubKey);
            PublicKey key = factory.generatePublic(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding");

            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytes = text.getBytes("UTF-8");

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            for (int i = 0; i < (bytes.length / 128 + 1); i++) {
                int start = i * 128;
                int blockLength;
                if (i == bytes.length / 128)
                    blockLength = bytes.length - i * 128;
                else
                    blockLength = 128;
                if (blockLength > 0) {
                    byte[] encrypted = cipher
                            .doFinal(bytes, start, blockLength);
                    baos.write(encrypted);
                }

            }

            byte[] encrypted = baos.toByteArray();
            return Base64.encodeToString(encrypted, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
