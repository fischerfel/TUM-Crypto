    public static String generate(String plaintext, String passphase) throws Exception {
        try {
            PBEKeySpec pbeKeySpec = new PBEKeySpec(passphase.toCharArray());
            PBEParameterSpec pbeParamSpec;
            SecretKeyFactory keyFac;
            // Salt
            byte[] salt = {(byte) 0xc8, (byte) 0x73, (byte) 0x61, (byte) 0x1d, (byte) 0x1a, (byte) 0xf2, (byte) 0xa8, (byte) 0x99};
            // Iteration count
            int count = 20;
            // Create PBE parameter set
            pbeParamSpec = new PBEParameterSpec(salt, count);
            keyFac = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
            SecretKey pbeKey = keyFac.generateSecret(pbeKeySpec);
            // Create PBE Cipher
            Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
            // Initialize PBE Cipher with key and parameters
            pbeCipher.init(Cipher.ENCRYPT_MODE, pbeKey, pbeParamSpec);
            // Our cleartext
            byte[] cleartext = plaintext.getBytes();
            // Encrypt the cleartext
            byte[] ciphertext = pbeCipher.doFinal(cleartext);
            return ciphertext;
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }
