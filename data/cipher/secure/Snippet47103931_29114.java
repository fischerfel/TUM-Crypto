        Cipher aesCBC = Cipher.getInstance("AES/CBC/Pkcs5Padding");
        MessageDigest md5 = MessageDigest.getInstance("MD5");

        // --- create key and IV  ---
        // the IV is useless, OpenSSL might as well have use zero's
        final byte[][] keyAndIV = EVP_BytesToKey(
                KEY_SIZE_BITS / 8,
                aesCBC.getBlockSize(),
                md5,
                salt,
                PASSWORD_STRING.getBytes("UTF-8"),
                ITERATIONS);
        SecretKeySpec key = new SecretKeySpec(keyAndIV[INDEX_KEY], "AES");
        IvParameterSpec iv = new IvParameterSpec(keyAndIV[INDEX_IV]);

        // --- initialize cipher instance and decrypt ---
        aesCBC.init(Cipher.DECRYPT_MODE, key, iv);
        byte[] decrypted = aesCBC.doFinal(encrypted);

        System.out.println(new String(decrypted, "UTF-8"));
