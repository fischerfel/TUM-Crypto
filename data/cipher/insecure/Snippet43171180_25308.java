public static void encrypt(Key key, byte[] content) throws Exception {
        Cipher cipher;
        byte[] encrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE,  key);
            encrypted = cipher.doFinal(content);
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveFile(encrypted);
        JOptionPane.showMessageDialog(null, "Encryption complete");
    }


 public static void decrypt(Key key, byte[] textCryp) throws Exception {
        Cipher cipher;
        byte[] decrypted = null;
        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, key);
            decrypted = cipher.doFinal(textCryp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveFile(decrypted);
        JOptionPane.showMessageDialog(null, "Decryption complete");
    }
