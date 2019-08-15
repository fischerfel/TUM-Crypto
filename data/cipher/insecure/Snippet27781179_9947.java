public final String encryptAES(final String plaintext, SecretKey key) {
        String ciphertext = new String();

        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] bytePlaintext = plaintext.getBytes();
            byte[] byteCiphertext = cipher.doFinal(bytePlaintext);
            ciphertext = new BASE64Encoder().encode(byteCiphertext);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException: " + e);
        } catch (NoSuchPaddingException e) {
            System.out.println("NoSuchPaddingException: " + e);
        } catch (InvalidKeyException e) {
            e.printStackTrace();
            System.out.println("InvalidKeyException: " + e);
        } catch (IllegalBlockSizeException e) {
            System.out.println("IllegalBlockSizeException: " + e);
        } catch (BadPaddingException e) {
            System.out.println("BadPaddingException: " + e);
        }
        return ciphertext;
    }

public final String decryptedPlain(String ciphertext, SecretKey key) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, key);


            byte [] decodedValue = new Base64().decode(ciphertext.getBytes());
            plaintext = cipher.doFinal(decodedValue);

        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException: " + e);
        } catch (NoSuchPaddingException e) {
            System.out.println("NoSuchPaddingException: " + e);
        } catch (InvalidKeyException e) {
            System.out.println("InvalidKeyException: " + e);
        } catch (IllegalBlockSizeException e) {
            System.out.println("IllegalBlockSizeException: " + e);
        } catch (BadPaddingException e) {
            System.out.println("BadPaddingException: " + e);
        }

        return new String(plaintext);
    }
