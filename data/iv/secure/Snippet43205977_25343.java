public static String decryptString(String src) throws Exception
    {
        String dst = "";

            SecretKey secret_key = KeyGenerator.getInstance("DES").generateKey();
            AlgorithmParameterSpec alogrithm_specs = new IvParameterSpec(initialization_vector);
            //encrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
            //encrypt.init(Cipher.ENCRYPT_MODE, secret_key, alogrithm_specs);
            decrypt = Cipher.getInstance("DES/CBC/PKCS5Padding");
            decrypt.init(Cipher.DECRYPT_MODE, secret_key, alogrithm_specs);
            //encrypt(new FileInputStream(plain), new FileOutputStream(cipher));
            //decrypt(new FileInputStream(encryptedFile), new FileOutputStream(decryptedFile));
            //System.out.println("End of Encryption/Decryption procedure!");
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            CipherOutputStream cout = new CipherOutputStream(baos,encrypt);
            cout.write(src.getBytes());
            cout.flush();               //ByteOutputStream -> Write Encryption Text
            cout.close(); 
            dst = DatatypeConverter.printHexBinary(baos.toByteArray());
            return dst;
    }
