public byte[] Decrypt(byte[] data)
    {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            byte[] cipherData = cipher.doFinal(data);
            return cipherData;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IllegalBlockSizeException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch(InvalidKeyException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch(BadPaddingException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    public byte[] Encrypt(byte[] data)
    {
        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            byte[] cipherData = cipher.doFinal(data);
            return cipherData;
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch(IllegalBlockSizeException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch(InvalidKeyException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        } catch(BadPaddingException ex) {
            Logger.getLogger(EncryptorDecryptor.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
