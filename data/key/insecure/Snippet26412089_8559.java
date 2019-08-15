public static String encrypt(String value) {
        byte[] encrypted = null;
        String encrypted_string = null;
        try {

            byte[] raw = new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
            Key skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] iv = new byte[cipher.getBlockSize()];

            IvParameterSpec ivParams = new IvParameterSpec(iv);
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec,ivParams);
            encrypted  = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string:" + encrypted.length);

            //Encrypted byte array
            System.out.println("encrypted byte array:" + encrypted);

            //Encrypted string
            encrypted_string = new String(encrypted);
            System.out.println("encrypted string: " + encrypted_string);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return encrypted_string;
    }

    public static String decrypt(String encrypted_string) {
        byte[] original = null;
        Cipher cipher = null;
        String decrypted_string = null;
        try {
            byte[] raw = new byte[]{'T', 'h', 'i', 's', 'I', 's', 'A', 'S', 'e', 'c', 'r', 'e', 't', 'K', 'e', 'y'};
            Key key = new SecretKeySpec(raw, "AES");
            cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //the block size (in bytes), or 0 if the underlying algorithm is not a block cipher
            byte[] ivByte = new byte[cipher.getBlockSize()];
            //This class specifies an initialization vector (IV). Examples which use
            //IVs are ciphers in feedback mode, e.g., DES in CBC mode and RSA ciphers with OAEP encoding operation.
            IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParamsSpec);
            original= cipher.doFinal(encrypted_string.getBytes());

            //Converts byte array to String
            decrypted_string = new String(original);
            System.out.println("Text Decrypted : " + decrypted_string);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return decrypted_string;
    }
