public static String decryptString(String cypherText, String password) {
        try {
            // decode the base64 cypherText into salt and encryptedString
            byte[] dataBase64 = DatatypeConverter.parseBase64Binary(cypherText);
            byte[] salt = Arrays.copyOfRange(dataBase64, SALT_OFFSET, SALT_OFFSET + SALT_SIZE);
            byte[] encrypted = Arrays.copyOfRange(dataBase64, CIPHERTEXT_OFFSET, dataBase64.length);
            System.out.println("dataBase64 = " + new String(dataBase64));
            System.out.println("salt: " + new BigInteger(1, salt).toString(16));
            System.out.println("encrypted: " + new BigInteger(1, encrypted).toString(16));    

            // --- specify cipher and digest for EVP_BytesToKey method ---
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            MessageDigest sha1 = MessageDigest.getInstance("SHA-256");

            // create key and IV
            final byte[][] keyAndIV = EVP_BytesToKey(
                    KEY_SIZE_BITS / Byte.SIZE,
                    cipher.getBlockSize(),
                    sha1,
                    salt,
                    password.getBytes("ASCII"),
                    ITERATIONS);
            SecretKeySpec key = new SecretKeySpec(keyAndIV[INDEX_KEY], "AES");
            IvParameterSpec iv = new IvParameterSpec(keyAndIV[INDEX_IV]);

            // initialize the Encryption Mode
            cipher.init(Cipher.DECRYPT_MODE, key, iv);

            // decrypt the message
            byte[] decrypted = cipher.doFinal(encrypted);
            String answer = new String(decrypted, "UTF-8"); // should this be "ASCII"?
            return answer;

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }
