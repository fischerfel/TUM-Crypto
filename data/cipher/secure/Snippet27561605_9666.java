public static void deriveKeyAndIV(String password)
            throws Exception
    {
        SecureRandom random = new SecureRandom();
        if (salt == null)
        {
            salt = new byte[HASH_BYTE_SIZE / 8]; // use salt size at least as long as hash
            random.nextBytes(salt);
        }
        if (ivBytes == null)
        {
            ivBytes = new byte[HASH_BYTE_SIZE / 8];
            random.nextBytes(ivBytes);
        }

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        keyBytes = skf.generateSecret(spec).getEncoded();
    }
public static byte[] encrypt(byte[] message) 
            throws Exception
    {
        // wrap key data in Key/IV specs to pass to cipher
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        //IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        // create the cipher with the algorithm you choose
        // see javadoc for Cipher class for more info, e.g.
        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");

        GCMParameterSpec gps = new GCMParameterSpec(128, ivBytes);

        cipher.init(Cipher.ENCRYPT_MODE, key, gps);
        byte[] encrypted = new byte[cipher.getOutputSize(message.length)];
        int enc_len = cipher.update(message, 0, message.length, encrypted, 0);
        enc_len += cipher.doFinal(encrypted, enc_len);
        return encrypted;
    }
public static byte[] decrypt(byte[] cipher_text) 
            throws Exception
    {
        // wrap key data in Key/IV specs to pass to cipher
        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
        // create the cipher with the algorithm you choose
        // see javadoc for Cipher class for more info, e.g.
        Cipher cipher = Cipher.getInstance("AES/GCM/PKCS5Padding");

        GCMParameterSpec gps = new GCMParameterSpec(128, ivBytes);

        cipher.init(Cipher.DECRYPT_MODE, key, gps);
        byte[] decrypted = new byte[cipher.getOutputSize(cipher_text.length)];
        int dec_len = cipher.update(cipher_text, 0, cipher_text.length, decrypted, 0);
        dec_len += cipher.doFinal(decrypted, dec_len);
        return decrypted;
    }
public static void main(String[] args) {
        String pass = "hello";
        try {
            deriveKeyAndIV(pass);
            byte[] tmp = encrypt("world!".getBytes());
            System.out.println(new String(Base64.getEncoder().encode(tmp)));
            System.out.println(new String(tmp));
            System.out.println("encrypted:\t" + bytesToHex(tmp));
            System.out.println("key:\t" + bytesToHex(keyBytes));
            System.out.println("iv:\t" + bytesToHex(ivBytes));
            tmp = decrypt(tmp);

            System.out.println("decrypted:\t" + bytesToHex(tmp));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
