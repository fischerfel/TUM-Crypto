public class SecureStorage {

    public String getPassword() {
        if(!isRooted()) {
            String password = pref.getPassword("");
            System.out.println("pass getPass: " + password);
            return password.isEmpty() ? password : new String(decrypt(Base64.decode(password, Base64.DEFAULT)));

        } else
            return "";
    }

    public void setPassword(String passwordStr) {
        if(!isRooted()) {
            byte[] password = encrypt(passwordStr.getBytes());
            pref.setPassword(password == null ? "" : Base64.encodeToString(password, Base64.DEFAULT));
        }
    }

    private SecretKey generateKey() {
        // Generate a 256-bit key
        final int outputKeyLength = 256;
        try {
            SecureRandom secureRandom = new SecureRandom();
            // Do *not* seed secureRandom! Automatically seeded from system entropy.
            KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
            keyGenerator.init(outputKeyLength, secureRandom);
            return keyGenerator.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private byte[] getRawKey(byte[] key) throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG", "Crypto");
        secureRandom.setSeed(key);
        keyGenerator.init(128, secureRandom); // 192 and 256 bits may not be available
        SecretKey secretKey = keyGenerator.generateKey();
        byte[] rawKey = secretKey.getEncoded();
        return rawKey;
    }

    /** The method that encrypts the string.
     @param toEncrypt The string to be encrypted.
     @return The encrypted string in bytes. */
    //****************************************
    private byte[] encrypt(byte[] toEncrypt) {
        byte[] encryptedByte = new String().getBytes();
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(getRawKey(Utils.generateUID().getBytes()), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            encryptedByte = cipher.doFinal(toEncrypt);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedByte;
    }

    //**************************************
    /** The method that decrypts the string.
     @param encryptedByte The string to be encrypted.
     @return The decrypted string in bytes. */
    //****************************************
    private byte[] decrypt(byte[] encryptedByte) {
        byte[] decryptedByte = new String().getBytes();
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(getRawKey(Utils.generateUID().getBytes()), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            decryptedByte = cipher.doFinal(encryptedByte);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedByte;
    }
}
