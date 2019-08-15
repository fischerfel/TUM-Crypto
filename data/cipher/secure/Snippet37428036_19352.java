private static String encrypt(String s, byte[] k) throws Exception {
        SecureRandom r = SecureRandom.getInstance("SHA1PRNG");
        // Generate 128 bit IV for Encryption
        byte[] iv = new byte[12]; r.nextBytes(iv);

        SecretKeySpec eks = new SecretKeySpec(k, "AES");
        Cipher c = Cipher.getInstance("AES/GCM/NoPadding");

        // Generated Authentication Tag should be 128 bits
        c.init(Cipher.ENCRYPT_MODE, eks, new GCMParameterSpec(128, iv));
        byte[] es = c.doFinal(s.getBytes(StandardCharsets.UTF_8));

        // Construct Output as "IV + CIPHERTEXT"
        byte[] os = new byte[12 + es.length];
        System.arraycopy(iv, 0, os, 0, 12);
        System.arraycopy(es, 0, os, 12, es.length);

        // Return a Base64 Encoded String
        return Base64.getEncoder().encodeToString(os);

    }

    private static String decrypt(String eos, byte[] k) throws Exception {
        // Recover our Byte Array by Base64 Decoding
        byte[] os = Base64.getDecoder().decode(eos);

        // Check Minimum Length (IV (12) + TAG (16))
        if (os.length > 28) {
            byte[] iv = Arrays.copyOfRange(os, 0, 12);
            byte[] es = Arrays.copyOfRange(os, 12, os.length);

            // Perform Decryption
            SecretKeySpec dks = new SecretKeySpec(k, "AES");
            Cipher c = Cipher.getInstance("AES/GCM/NoPadding");
            c.init(Cipher.DECRYPT_MODE, dks, new GCMParameterSpec(128, iv));

            // Return our Decrypted String
            return new String(c.doFinal(es), StandardCharsets.UTF_8);
        }
        throw new Exception();
    }
