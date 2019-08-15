public class AES {

    public String getEncrypt(String pass){
        String password = encrypt(pass);
        return password;
    }

    public String getDecrypt(String pass){
        String key = "AesSEcREtkeyABCD";
        byte[] passwordByte = decrypt(key,pass);
        String password = new String(passwordByte);
        return password;
    }

    private byte[] decrypt(String key, String encrypted) {
        try {
            SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(skeySpec.getEncoded(), "AES"));
            //getting error here
            byte[] original = cipher.doFinal(encrypted.getBytes());
            return original;
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (NoSuchPaddingException ex) {
            ex.printStackTrace();
        }
        return null;
    } 

    private String encrypt(String value) {
        try {
            byte[] raw = new byte[]{'A', 'e', 's', 'S', 'E', 'c', 'R', 'E', 't', 'k', 'e', 'y','A','B','C','D'};
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
            byte[] encrypted = cipher.doFinal(value.getBytes());
            System.out.println("encrypted string:" + (new String(encrypted)));
            return new String(encrypted);
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        } catch (IllegalBlockSizeException ex) {
            ex.printStackTrace();       
        } catch (BadPaddingException ex) {
            ex.printStackTrace();
        } catch (InvalidKeyException ex) {
            ex.printStackTrace();
        } catch (NoSuchPaddingException ex) {
            ex.printStackTrace();
        }
        return null;
    }
